/**
 * Copyright 2016 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package io.reactivex.subjects;

import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.*;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.observers.BasicIntQueueDisposable;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;

/**
 * Subject that allows only a single Subscriber to subscribe to it during its lifetime.
 *
 * <p>This subject buffers notifications and replays them to the Subscriber as requested.
 *
 * <p>This subject holds an unbounded internal buffer.
 *
 * <p>If more than one Subscriber attempts to subscribe to this Subject, they
 * will receive an IllegalStateException if this Subject hasn't terminated yet,
 * or the Subscribers receive the terminal event (error or completion) if this
 * Subject has terminated.
 *
 * @param <T> the value type received and emitted by this Subject subclass
 * @since 2.0
 */
public final class UnicastSubject<T> extends Subject<T> {
    /** The queue that buffers the source events. */
    final SpscLinkedArrayQueue<T> queue;

    /** The single Observer. */
    final AtomicReference<Observer<? super T>> actual;

    /** The optional callback when the Subject gets cancelled or terminates. */
    final AtomicReference<Runnable> onTerminate;

    /** Indicates the single observer has cancelled. */
    volatile boolean disposed;

    /** Indicates the source has terminated. */
    volatile boolean done;
    /**
     * The terminal error if not null.
     * Must be set before writing to done and read after done == true.
     */
    Throwable error;

    /** Set to 1 atomically for the first and only Subscriber. */
    final AtomicBoolean once;

    /** The wip counter and QueueDisposable surface. */
    final BasicIntQueueDisposable<T> wip;

    boolean enableOperatorFusion;

    /**
     * Creates an UnicastSubject with an internal buffer capacity hint 16.
     * @param <T> the value type
     * @return an UnicastSubject instance
     */
    public static <T> UnicastSubject<T> create() {
        return new UnicastSubject<T>(bufferSize());
    }

    /**
     * Creates an UnicastSubject with the given internal buffer capacity hint.
     * @param <T> the value type
     * @param capacityHint the hint to size the internal unbounded buffer
     * @return an UnicastSubject instance
     */
    public static <T> UnicastSubject<T> create(int capacityHint) {
        return new UnicastSubject<T>(capacityHint);
    }

    /**
     * Creates an UnicastSubject with the given internal buffer capacity hint and a callback for
     * the case when the single Subscriber cancels its subscription.
     *
     * <p>The callback, if not null, is called exactly once and
     * non-overlapped with any active replay.
     *
     * @param <T> the value type
     * @param capacityHint the hint to size the internal unbounded buffer
     * @param onCancelled the non null callback
     * @return an UnicastSubject instance
     */
    public static <T> UnicastSubject<T> create(int capacityHint, Runnable onCancelled) {
        return new UnicastSubject<T>(capacityHint, onCancelled);
    }

    /**
     * Creates an UnicastSubject with the given capacity hint.
     * @param capacityHint the capacity hint for the internal, unbounded queue
     * @since 2.0
     */
    UnicastSubject(int capacityHint) {
        this.queue = new SpscLinkedArrayQueue<T>(ObjectHelper.verifyPositive(capacityHint, "capacityHint"));
        this.onTerminate = new AtomicReference<Runnable>();
        this.actual = new AtomicReference<Observer<? super T>>();
        this.once = new AtomicBoolean();
        this.wip = new UnicastQueueDisposable();
    }

    /**
     * Creates an UnicastProcessor with the given capacity hint and callback
     * for when the Processor is terminated normally or its single Subscriber cancels.
     * @param capacityHint the capacity hint for the internal, unbounded queue
     * @param onTerminate the callback to run when the Processor is terminated or cancelled, null not allowed
     * @since 2.0
     */
    UnicastSubject(int capacityHint, Runnable onTerminate) {
        this.queue = new SpscLinkedArrayQueue<T>(ObjectHelper.verifyPositive(capacityHint, "capacityHint"));
        this.onTerminate = new AtomicReference<Runnable>(ObjectHelper.requireNonNull(onTerminate, "onTerminate"));
        this.actual = new AtomicReference<Observer<? super T>>();
        this.once = new AtomicBoolean();
        this.wip = new UnicastQueueDisposable();
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        if (!once.get() && once.compareAndSet(false, true)) {
            observer.onSubscribe(wip);
            actual.lazySet(observer); // full barrier in drain
            if (disposed) {
                actual.lazySet(null);
                return;
            }
            drain();
        } else {
            EmptyDisposable.error(new IllegalStateException("Only a single observer allowed."), observer);
        }
    }

    void doTerminate() {
        Runnable r = onTerminate.get();
        if (r != null && onTerminate.compareAndSet(r, null)) {
            r.run();
        }
    }

    @Override
    public void onSubscribe(Disposable s) {
        if (done || disposed) {
            s.dispose();
        }
    }

    @Override
    public void onNext(T t) {
        if (done || disposed) {
            return;
        }
        if (t == null) {
            onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
            return;
        }
        queue.offer(t);
        drain();
    }

    @Override
    public void onError(Throwable t) {
        if (done || disposed) {
            RxJavaPlugins.onError(t);
            return;
        }
        if (t == null) {
            t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        }
        error = t;
        done = true;

        doTerminate();

        drain();
    }

    @Override
    public void onComplete() {
        if (done || disposed) {
            return;
        }
        done = true;

        doTerminate();

        drain();
    }

    void drainNormal(Observer<? super T> a) {
        int missed = 1;
        SimpleQueue<T> q = queue;
        for (;;) {
            for (;;) {

                if (disposed) {
                    actual.lazySet(null);
                    q.clear();
                    return;
                }

                boolean d = done;
                T v = queue.poll();
                boolean empty = v == null;

                if (d && empty) {
                    actual.lazySet(null);
                    Throwable ex = error;
                    if (ex != null) {
                        a.onError(ex);
                    } else {
                        a.onComplete();
                    }
                    return;
                }

                if (empty) {
                    break;
                }

                a.onNext(v);
            }

            missed = wip.addAndGet(-missed);
            if (missed == 0) {
                break;
            }
        }
    }

    void drainFused(Observer<? super T> a) {
        int missed = 1;

        final SpscLinkedArrayQueue<T> q = queue;

        for (;;) {

            if (disposed) {
                actual.lazySet(null);
                q.clear();
                return;
            }

            boolean d = done;

            a.onNext(null);

            if (d) {
                actual.lazySet(null);

                Throwable ex = error;
                if (ex != null) {
                    a.onError(ex);
                } else {
                    a.onComplete();
                }
                return;
            }

            missed = wip.addAndGet(-missed);
            if (missed == 0) {
                break;
            }
        }
    }

    void drain() {
        if (wip.getAndIncrement() != 0) {
            return;
        }

        Observer<? super T> a = actual.get();
        int missed = 1;

        for (;;) {

            if (a != null) {
                if (enableOperatorFusion) {
                    drainFused(a);
                } else {
                    drainNormal(a);
                }
                return;
            }

            missed = wip.addAndGet(-missed);
            if (missed == 0) {
                break;
            }

            a = actual.get();
        }
    }

    @Override
    public boolean hasObservers() {
        return actual.get() != null;
    }

    @Override
    public Throwable getThrowable() {
        if (done) {
            return error;
        }
        return null;
    }

    @Override
    public boolean hasThrowable() {
        return done && error != null;
    }

    @Override
    public boolean hasComplete() {
        return done && error == null;
    }

    final class UnicastQueueDisposable extends BasicIntQueueDisposable<T> {


        private static final long serialVersionUID = 7926949470189395511L;

        @Override
        public int requestFusion(int mode) {
            if ((mode & ASYNC) != 0) {
                enableOperatorFusion = true;
                return ASYNC;
            }
            return NONE;
        }

        @Override
        public T poll() throws Exception {
            return queue.poll();
        }

        @Override
        public boolean isEmpty() {
            return queue.isEmpty();
        }

        @Override
        public void clear() {
            queue.clear();
        }

        @Override
        public void dispose() {
            if (!disposed) {
                disposed = true;

                doTerminate();

                actual.lazySet(null);
                if (wip.getAndIncrement() == 0) {
                    actual.lazySet(null);
                    queue.clear();
                }
            }
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }

    }
}

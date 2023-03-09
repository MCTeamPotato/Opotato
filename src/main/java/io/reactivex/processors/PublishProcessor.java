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
package io.reactivex.processors;

import java.util.concurrent.atomic.*;

import org.reactivestreams.*;

import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * A Subject that multicasts events to Subscribers that are currently subscribed to it.
 *
 * <p>
 * <img width="640" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/S.PublishSubject.png" alt="">
 *
 * <p>The subject does not coordinate backpressure for its subscribers and implements a weaker onSubscribe which
 * calls requests Long.MAX_VALUE from the incoming Subscriptions. This makes it possible to subscribe the PublishSubject
 * to multiple sources (note on serialization though) unlike the standard contract on Subscriber. Child subscribers, however, are not overflown but receive an
 * IllegalStateException in case their requested amount is zero.
 *
 * <p>The implementation of onXXX methods are technically thread-safe but non-serialized calls
 * to them may lead to undefined state in the currently subscribed Subscribers.
 *
 * <p>Due to the nature Flowables are constructed, the PublishProcessor can't be instantiated through
 * {@code new} but must be created via the {@link #create()} method.
 *
 * Example usage:
 * <p>
 * <pre> {@code

  PublishProcessor<Object> processor = PublishProcessor.create();
  // subscriber1 will receive all onNext and onComplete events
  processor.subscribe(subscriber1);
  processor.onNext("one");
  processor.onNext("two");
  // subscriber2 will only receive "three" and onComplete
  processor.subscribe(subscriber2);
  processor.onNext("three");
  processor.onComplete();

  } </pre>
 * @param <T> the value type multicast to Subscribers.
 */
public final class PublishProcessor<T> extends FlowableProcessor<T> {
    /** The terminated indicator for the subscribers array. */
    @SuppressWarnings("rawtypes")
    static final PublishSubscription[] TERMINATED = new PublishSubscription[0];
    /** An empty subscribers array to avoid allocating it all the time. */
    @SuppressWarnings("rawtypes")
    static final PublishSubscription[] EMPTY = new PublishSubscription[0];

    /** The array of currently subscribed subscribers. */
    final AtomicReference<PublishSubscription<T>[]> subscribers;

    /** The error, write before terminating and read after checking subscribers. */
    Throwable error;

    /**
     * Constructs a PublishProcessor.
     * @param <T> the value type
     * @return the new PublishProcessor
     */
    public static <T> PublishProcessor<T> create() {
        return new PublishProcessor<T>();
    }

    /**
     * Constructs a PublishProcessor.
     * @since 2.0
     */
    @SuppressWarnings("unchecked")
    PublishProcessor() {
        subscribers = new AtomicReference<PublishSubscription<T>[]>(EMPTY);
    }


    @Override
    public void subscribeActual(Subscriber<? super T> t) {
        PublishSubscription<T> ps = new PublishSubscription<T>(t, this);
        t.onSubscribe(ps);
        if (add(ps)) {
            // if cancellation happened while a successful add, the remove() didn't work
            // so we need to do it again
            if (ps.isCancelled()) {
                remove(ps);
            }
        } else {
            Throwable ex = error;
            if (ex != null) {
                t.onError(ex);
            } else {
                t.onComplete();
            }
        }
    }

    /**
     * Tries to add the given subscriber to the subscribers array atomically
     * or returns false if the subject has terminated.
     * @param ps the subscriber to add
     * @return true if successful, false if the subject has terminated
     */
    boolean add(PublishSubscription<T> ps) {
        for (;;) {
            PublishSubscription<T>[] a = subscribers.get();
            if (a == TERMINATED) {
                return false;
            }

            int n = a.length;
            @SuppressWarnings("unchecked")
            PublishSubscription<T>[] b = new PublishSubscription[n + 1];
            System.arraycopy(a, 0, b, 0, n);
            b[n] = ps;

            if (subscribers.compareAndSet(a, b)) {
                return true;
            }
        }
    }

    /**
     * Atomically removes the given subscriber if it is subscribed to the subject.
     * @param ps the subject to remove
     */
    @SuppressWarnings("unchecked")
    void remove(PublishSubscription<T> ps) {
        for (;;) {
            PublishSubscription<T>[] a = subscribers.get();
            if (a == TERMINATED || a == EMPTY) {
                return;
            }

            int n = a.length;
            int j = -1;
            for (int i = 0; i < n; i++) {
                if (a[i] == ps) {
                    j = i;
                    break;
                }
            }

            if (j < 0) {
                return;
            }

            PublishSubscription<T>[] b;

            if (n == 1) {
                b = EMPTY;
            } else {
                b = new PublishSubscription[n - 1];
                System.arraycopy(a, 0, b, 0, j);
                System.arraycopy(a, j + 1, b, j, n - j - 1);
            }
            if (subscribers.compareAndSet(a, b)) {
                return;
            }
        }
    }

    @Override
    public void onSubscribe(Subscription s) {
        if (subscribers.get() == TERMINATED) {
            s.cancel();
            return;
        }
        // PublishSubject doesn't bother with request coordination.
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(T t) {
        if (subscribers.get() == TERMINATED) {
            return;
        }
        if (t == null) {
            onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
            return;
        }
        for (PublishSubscription<T> s : subscribers.get()) {
            s.onNext(t);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onError(Throwable t) {
        if (subscribers.get() == TERMINATED) {
            RxJavaPlugins.onError(t);
            return;
        }
        if (t == null) {
            t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        }
        error = t;

        for (PublishSubscription<T> s : subscribers.getAndSet(TERMINATED)) {
            s.onError(t);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onComplete() {
        if (subscribers.get() == TERMINATED) {
            return;
        }
        for (PublishSubscription<T> s : subscribers.getAndSet(TERMINATED)) {
            s.onComplete();
        }
    }

    @Override
    public boolean hasSubscribers() {
        return subscribers.get().length != 0;
    }

    @Override
    public Throwable getThrowable() {
        if (subscribers.get() == TERMINATED) {
            return error;
        }
        return null;
    }

    @Override
    public boolean hasThrowable() {
        return subscribers.get() == TERMINATED && error != null;
    }

    @Override
    public boolean hasComplete() {
        return subscribers.get() == TERMINATED && error == null;
    }

    /**
     * Wraps the actual subscriber, tracks its requests and makes cancellation
     * to remove itself from the current subscribers array.
     *
     * @param <T> the value type
     */
    static final class PublishSubscription<T> extends AtomicLong implements Subscription {

        private static final long serialVersionUID = 3562861878281475070L;
        /** The actual subscriber. */
        final Subscriber<? super T> actual;
        /** The subject state. */
        final PublishProcessor<T> parent;

        /**
         * Constructs a PublishSubscriber, wraps the actual subscriber and the state.
         * @param actual the actual subscriber
         * @param parent the parent PublishProcessor
         */
        PublishSubscription(Subscriber<? super T> actual, PublishProcessor<T> parent) {
            this.actual = actual;
            this.parent = parent;
        }

        public void onNext(T t) {
            long r = get();
            if (r == Long.MIN_VALUE) {
                return;
            }
            if (r != 0L) {
                actual.onNext(t);
                if (r != Long.MAX_VALUE) {
                    decrementAndGet();
                }
            } else {
                cancel();
                actual.onError(new MissingBackpressureException("Could not emit value due to lack of requests"));
            }
        }

        public void onError(Throwable t) {
            if (get() != Long.MIN_VALUE) {
                actual.onError(t);
            } else {
                RxJavaPlugins.onError(t);
            }
        }

        public void onComplete() {
            if (get() != Long.MIN_VALUE) {
                actual.onComplete();
            }
        }

        @Override
        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.addCancel(this, n);
            }
        }

        @Override
        public void cancel() {
            if (getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
                parent.remove(this);
            }
        }

        public boolean isCancelled() {
            return get() == Long.MIN_VALUE;
        }
    }
}

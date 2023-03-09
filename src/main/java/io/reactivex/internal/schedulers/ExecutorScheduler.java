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

package io.reactivex.internal.schedulers;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import io.reactivex.Scheduler;
import io.reactivex.disposables.*;
import io.reactivex.internal.disposables.*;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.schedulers.ExecutorScheduler.ExecutorWorker.BooleanRunnable;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

/**
 * Wraps an Executor and provides the Scheduler API over it.
 */
public final class ExecutorScheduler extends Scheduler {

    final Executor executor;

    static final Scheduler HELPER = Schedulers.single();

    public ExecutorScheduler(Executor executor) {
        this.executor = executor;
    }

    @Override
    public Worker createWorker() {
        return new ExecutorWorker(executor);
    }

    @Override
    public Disposable scheduleDirect(Runnable run) {
        Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
        try {
            if (executor instanceof ExecutorService) {
                Future<?> f = ((ExecutorService)executor).submit(decoratedRun);
                return Disposables.fromFuture(f);
            }

            BooleanRunnable br = new BooleanRunnable(decoratedRun);
            executor.execute(br);
            return br;
        } catch (RejectedExecutionException ex) {
            RxJavaPlugins.onError(ex);
            return EmptyDisposable.INSTANCE;
        }
    }

    @Override
    public Disposable scheduleDirect(Runnable run, long delay, TimeUnit unit) {
        final Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
        if (executor instanceof ScheduledExecutorService) {
            try {
                Future<?> f = ((ScheduledExecutorService)executor).schedule(decoratedRun, delay, unit);
                return Disposables.fromFuture(f);
            } catch (RejectedExecutionException ex) {
                RxJavaPlugins.onError(ex);
                return EmptyDisposable.INSTANCE;
            }
        }
        SequentialDisposable first = new SequentialDisposable();

        final SequentialDisposable mar = new SequentialDisposable(first);

        Disposable delayed = HELPER.scheduleDirect(new Runnable() {
            @Override
            public void run() {
                mar.replace(scheduleDirect(decoratedRun));
            }
        }, delay, unit);

        first.replace(delayed);

        return mar;
    }

    @Override
    public Disposable schedulePeriodicallyDirect(Runnable run, long initialDelay, long period, TimeUnit unit) {
        if (executor instanceof ScheduledExecutorService) {
            Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
            try {
                Future<?> f = ((ScheduledExecutorService)executor).scheduleAtFixedRate(decoratedRun, initialDelay, period, unit);
                return Disposables.fromFuture(f);
            } catch (RejectedExecutionException ex) {
                RxJavaPlugins.onError(ex);
                return EmptyDisposable.INSTANCE;
            }
        }
        return super.schedulePeriodicallyDirect(run, initialDelay, period, unit);
    }
    /* public: test support. */
    public static final class ExecutorWorker extends Scheduler.Worker implements Runnable {
        final Executor executor;

        final MpscLinkedQueue<Runnable> queue;

        volatile boolean disposed;

        final AtomicInteger wip = new AtomicInteger();

        final CompositeDisposable tasks = new CompositeDisposable();

        public ExecutorWorker(Executor executor) {
            this.executor = executor;
            this.queue = new MpscLinkedQueue<Runnable>();
        }

        @Override
        public Disposable schedule(Runnable run) {
            if (disposed) {
                return EmptyDisposable.INSTANCE;
            }

            Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
            BooleanRunnable br = new BooleanRunnable(decoratedRun);

            queue.offer(br);

            if (wip.getAndIncrement() == 0) {
                try {
                    executor.execute(this);
                } catch (RejectedExecutionException ex) {
                    disposed = true;
                    queue.clear();
                    RxJavaPlugins.onError(ex);
                    return EmptyDisposable.INSTANCE;
                }
            }

            return br;
        }

        @Override
        public Disposable schedule(Runnable run, long delay, TimeUnit unit) {
            if (delay <= 0) {
                return schedule(run);
            }
            if (disposed) {
                return EmptyDisposable.INSTANCE;
            }


            SequentialDisposable first = new SequentialDisposable();

            final SequentialDisposable mar = new SequentialDisposable(first);

            final Runnable decoratedRun = RxJavaPlugins.onSchedule(run);

            ScheduledRunnable sr = new ScheduledRunnable(new Runnable() {
                @Override
                public void run() {
                    mar.replace(schedule(decoratedRun));
                }
            }, tasks);
            tasks.add(sr);

            if (executor instanceof ScheduledExecutorService) {
                try {
                    Future<?> f = ((ScheduledExecutorService)executor).schedule((Callable<Object>)sr, delay, unit);
                    sr.setFuture(f);
                } catch (RejectedExecutionException ex) {
                    disposed = true;
                    RxJavaPlugins.onError(ex);
                    return EmptyDisposable.INSTANCE;
                }
            } else {
                final Disposable d = HELPER.scheduleDirect(sr, delay, unit);
                sr.setFuture(new DisposeOnCancel(d));
            }

            first.replace(sr);

            return mar;
        }

        @Override
        public void dispose() {
            if (!disposed) {
                disposed = true;
                tasks.dispose();
                if (wip.getAndIncrement() == 0) {
                    queue.clear();
                }
            }
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }

        @Override
        public void run() {
            int missed = 1;
            final MpscLinkedQueue<Runnable> q = queue;
            for (;;) {

                if (disposed) {
                    q.clear();
                    return;
                }

                for (;;) {
                    Runnable run = q.poll();
                    if (run == null) {
                        break;
                    }
                    run.run();

                    if (disposed) {
                        q.clear();
                        return;
                    }
                }

                if (disposed) {
                    q.clear();
                    return;
                }

                missed = wip.addAndGet(-missed);
                if (missed == 0) {
                    break;
                }
            }
        }

        static final class BooleanRunnable extends AtomicBoolean implements Runnable, Disposable {

            private static final long serialVersionUID = -2421395018820541164L;

            final Runnable actual;
            BooleanRunnable(Runnable actual) {
                this.actual = actual;
            }

            @Override
            public void run() {
                if (get()) {
                    return;
                }
                actual.run();
            }

            @Override
            public void dispose() {
                lazySet(true);
            }

            @Override
            public boolean isDisposed() {
                return get();
            }
        }
    }

}

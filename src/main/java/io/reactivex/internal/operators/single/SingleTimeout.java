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

package io.reactivex.internal.operators.single;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.*;
import io.reactivex.disposables.*;

public final class SingleTimeout<T> extends Single<T> {

    final SingleSource<T> source;

    final long timeout;

    final TimeUnit unit;

    final Scheduler scheduler;

    final SingleSource<? extends T> other;

    public SingleTimeout(SingleSource<T> source, long timeout, TimeUnit unit, Scheduler scheduler,
                         SingleSource<? extends T> other) {
        this.source = source;
        this.timeout = timeout;
        this.unit = unit;
        this.scheduler = scheduler;
        this.other = other;
    }

    @Override
    protected void subscribeActual(final SingleObserver<? super T> s) {

        final CompositeDisposable set = new CompositeDisposable();
        s.onSubscribe(set);

        final AtomicBoolean once = new AtomicBoolean();

        Disposable timer = scheduler.scheduleDirect(new Runnable() {
            @Override
            public void run() {
                if (once.compareAndSet(false, true)) {
                    if (other != null) {
                        set.clear();
                        other.subscribe(new SingleObserver<T>() {

                            @Override
                            public void onError(Throwable e) {
                                set.dispose();
                                s.onError(e);
                            }

                            @Override
                            public void onSubscribe(Disposable d) {
                                set.add(d);
                            }

                            @Override
                            public void onSuccess(T value) {
                                set.dispose();
                                s.onSuccess(value);
                            }

                        });
                    } else {
                        set.dispose();
                        s.onError(new TimeoutException());
                    }
                }
            }
        }, timeout, unit);

        set.add(timer);

        source.subscribe(new SingleObserver<T>() {

            @Override
            public void onError(Throwable e) {
                if (once.compareAndSet(false, true)) {
                    set.dispose();
                    s.onError(e);
                }
            }

            @Override
            public void onSubscribe(Disposable d) {
                set.add(d);
            }

            @Override
            public void onSuccess(T value) {
                if (once.compareAndSet(false, true)) {
                    set.dispose();
                    s.onSuccess(value);
                }
            }

        });

    }

}

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

package io.reactivex;

import java.util.concurrent.*;

import org.reactivestreams.Publisher;

import io.reactivex.annotations.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.*;
import io.reactivex.internal.functions.*;
import io.reactivex.internal.fuseable.*;
import io.reactivex.internal.observers.*;
import io.reactivex.internal.operators.completable.*;
import io.reactivex.internal.operators.flowable.*;
import io.reactivex.internal.operators.maybe.*;
import io.reactivex.internal.operators.observable.*;
import io.reactivex.internal.operators.single.*;
import io.reactivex.internal.util.*;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

/**
 * The Single class implements the Reactive Pattern for a single value response.
 * See {@link Flowable} or {@link Observable} for the
 * implementation of the Reactive Pattern for a stream or vector of values.
 * <p>
 * {@code Single} behaves the same as {@link Observable} except that it can only emit either a single successful
 * value, or an error (there is no "onComplete" notification as there is for {@link Observable})
 * <p>
 * Like an {@link Observable}, a {@code Single} is lazy, can be either "hot" or "cold", synchronous or
 * asynchronous.
 * <p>
 * The documentation for this class makes use of marble diagrams. The following legend explains these diagrams:
 * <p>
 * <img width="605" height="285" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.legend.png" alt="">
 * <p>
 * For more information see the <a href="http://reactivex.io/documentation/observable.html">ReactiveX
 * documentation</a>.
 *
 * @param <T>
 *            the type of the item emitted by the Single
 * @since 2.0
 */
public abstract class Single<T> implements SingleSource<T> {

    /**
     * Runs multiple Single sources and signals the events of the first one that signals (cancelling
     * the rest).
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code amb} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <T> the value type
     * @param sources the Iterable sequence of sources
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Single<T> amb(final Iterable<? extends SingleSource<? extends T>> sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly(new SingleAmb<T>(null, sources));
    }

    /**
     * Runs multiple Single sources and signals the events of the first one that signals (cancelling
     * the rest).
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code ambArray} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <T> the value type
     * @param sources the array of sources
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings("unchecked")
    public static <T> Single<T> ambArray(final SingleSource<? extends T>... sources) {
        if (sources.length == 0) {
            return error(SingleInternalHelper.<T>emptyThrower());
        }
        if (sources.length == 1) {
            return wrap((SingleSource<T>)sources[0]);
        }
        return RxJavaPlugins.onAssembly(new SingleAmb<T>(sources, null));
    }

    /**
     * Concatenate the single values, in a non-overlapping fashion, of the Single sources provided by
     * an Iterable sequence.
     * <dl>
     *  <dt><b>Backpressure:</b><dt>
     *  <dd>The returned {@code Flowable} honors the backpressure of the downstream consumer.</dd>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code concat} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <T> the value type
     * @param sources the Iterable sequence of SingleSource instances
     * @return the new Flowable instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    @BackpressureSupport(BackpressureKind.FULL)
    public static <T> Flowable<T> concat(Iterable<? extends SingleSource<? extends T>> sources) {
        return concat(Flowable.fromIterable(sources));
    }

    /**
     * Concatenate the single values, in a non-overlapping fashion, of the Single sources provided by
     * an Observable sequence.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code concat} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <T> the value type
     * @param sources the ObservableSource of SingleSource instances
     * @return the new Observable instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> Observable<T> concat(ObservableSource<? extends SingleSource<? extends T>> sources) {
        return RxJavaPlugins.onAssembly(new ObservableConcatMap(sources, SingleInternalHelper.toObservable(), 2, ErrorMode.IMMEDIATE));
    }

    /**
     * Concatenate the single values, in a non-overlapping fashion, of the Single sources provided by
     * a Publisher sequence.
     * <dl>
     *  <dt><b>Backpressure:</b><dt>
     *  <dd>The returned {@code Flowable} honors the backpressure of the downstream consumer
     *  and the sources {@code Publisher} is expected to honor it as well.</dd>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code concat} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <T> the value type
     * @param sources the Publisher of SingleSource instances
     * @return the new Flowable instance
     * @since 2.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Flowable<T> concat(Publisher<? extends SingleSource<? extends T>> sources) {
        return concat(sources, 2);
    }

    /**
     * Concatenate the single values, in a non-overlapping fashion, of the Single sources provided by
     * a Publisher sequence and prefetched by the specified amount.
     * <dl>
     *  <dt><b>Backpressure:</b><dt>
     *  <dd>The returned {@code Flowable} honors the backpressure of the downstream consumer
     *  and the sources {@code Publisher} is expected to honor it as well.</dd>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code concat} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <T> the value type
     * @param sources the Publisher of SingleSource instances
     * @param prefetch the number of SingleSources to prefetch from the Publisher
     * @return the new Flowable instance
     * @since 2.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> Flowable<T> concat(Publisher<? extends SingleSource<? extends T>> sources, int prefetch) {
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly(new FlowableConcatMap(sources, SingleInternalHelper.toFlowable(), prefetch, ErrorMode.IMMEDIATE));
    }

    /**
     * Returns a Flowable that emits the items emitted by two Singles, one after the other.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.concat.png" alt="">
     * <dl>
     *  <dt><b>Backpressure:</b><dt>
     *  <dd>The returned {@code Flowable} honors the backpressure of the downstream consumer.</dd>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code concat} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <T> the common value type
     * @param source1
     *            a Single to be concatenated
     * @param source2
     *            a Single to be concatenated
     * @return a Flowable that emits items emitted by the two source Singles, one after the other.
     * @see <a href="http://reactivex.io/documentation/operators/concat.html">ReactiveX operators documentation: Concat</a>
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings("unchecked")
    public static <T> Flowable<T> concat(
            SingleSource<? extends T> source1, SingleSource<? extends T> source2
     ) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return concat(Flowable.fromArray(source1, source2));
    }

    /**
     * Returns a Flowable that emits the items emitted by three Singles, one after the other.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.concat.png" alt="">
     * <dl>
     *  <dt><b>Backpressure:</b><dt>
     *  <dd>The returned {@code Flowable} honors the backpressure of the downstream consumer.</dd>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code concat} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <T> the common value type
     * @param source1
     *            a Single to be concatenated
     * @param source2
     *            a Single to be concatenated
     * @param source3
     *            a Single to be concatenated
     * @return a Flowable that emits items emitted by the three source Singles, one after the other.
     * @see <a href="http://reactivex.io/documentation/operators/concat.html">ReactiveX operators documentation: Concat</a>
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings("unchecked")
    public static <T> Flowable<T> concat(
            SingleSource<? extends T> source1, SingleSource<? extends T> source2,
            SingleSource<? extends T> source3
     ) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return concat(Flowable.fromArray(source1, source2, source3));
    }

    /**
     * Returns a Flowable that emits the items emitted by four Singles, one after the other.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.concat.png" alt="">
     * <dl>
     *  <dt><b>Backpressure:</b><dt>
     *  <dd>The returned {@code Flowable} honors the backpressure of the downstream consumer.</dd>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code concat} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <T> the common value type
     * @param source1
     *            a Single to be concatenated
     * @param source2
     *            a Single to be concatenated
     * @param source3
     *            a Single to be concatenated
     * @param source4
     *            a Single to be concatenated
     * @return a Flowable that emits items emitted by the four source Singles, one after the other.
     * @see <a href="http://reactivex.io/documentation/operators/concat.html">ReactiveX operators documentation: Concat</a>
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings("unchecked")
    public static <T> Flowable<T> concat(
            SingleSource<? extends T> source1, SingleSource<? extends T> source2,
            SingleSource<? extends T> source3, SingleSource<? extends T> source4
     ) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return concat(Flowable.fromArray(source1, source2, source3, source4));
    }

    /**
     * Concatenate the single values, in a non-overlapping fashion, of the Single sources provided in
     * an array.
     * <dl>
     *  <dt><b>Backpressure:</b><dt>
     *  <dd>The returned {@code Flowable} honors the backpressure of the downstream consumer.</dd>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code concatArray} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <T> the value type
     * @param sources the array of SingleSource instances
     * @return the new Flowable instance
     * @since 2.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> Flowable<T> concatArray(SingleSource<? extends T>... sources) {
        return RxJavaPlugins.onAssembly(new FlowableConcatMap(Flowable.fromArray(sources), SingleInternalHelper.toFlowable(), 2, ErrorMode.BOUNDARY));
    }

    /**
     * Provides an API (via a cold Completable) that bridges the reactive world with the callback-style world.
     * <p>
     * Example:
     * <pre><code>
     * Single.&lt;Event&gt;create(emitter -&gt; {
     *     Callback listener = new Callback() {
     *         &#64;Override
     *         public void onEvent(Event e) {
     *             emitter.onSuccess(e);
     *         }
     *
     *         &#64;Override
     *         public void onFailure(Exception e) {
     *             emitter.onError(e);
     *         }
     *     };
     *
     *     AutoCloseable c = api.someMethod(listener);
     *
     *     emitter.setCancellable(c::close);
     *
     * });
     * </code></pre>
     * <p>
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <T> the value type
     * @param source the emitter that is called when a SingleObserver subscribes to the returned {@code Single}
     * @return the new Single instance
     * @see SingleOnSubscribe
     * @see Cancellable
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Single<T> create(SingleOnSubscribe<T> source) {
        ObjectHelper.requireNonNull(source, "source is null");
        return RxJavaPlugins.onAssembly(new SingleCreate<T>(source));
    }

    /**
     * Calls a Callable for each individual SingleObserver to return the actual Single source to
     * be subscribed to.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code defer} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <T> the value type
     * @param singleSupplier the Callable that is called for each individual SingleObserver and
     * returns a SingleSource instance to subscribe to
     * @return the new Single instance
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Single<T> defer(final Callable<? extends SingleSource<? extends T>> singleSupplier) {
        ObjectHelper.requireNonNull(singleSupplier, "singleSupplier is null");
        return RxJavaPlugins.onAssembly(new SingleDefer<T>(singleSupplier));
    }

    /**
     * Signals a Throwable returned by the callback function for each individual SingleObserver.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code error} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <T> the value type
     * @param errorSupplier the callable that is called for each individual SingleObserver and
     * returns a Throwable instance to be emitted.
     * @return the new Single instance
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Single<T> error(final Callable<? extends Throwable> errorSupplier) {
        ObjectHelper.requireNonNull(errorSupplier, "errorSupplier is null");
        return RxJavaPlugins.onAssembly(new SingleError<T>(errorSupplier));
    }

    /**
     * Returns a Single that invokes a subscriber's {@link SingleObserver#onError onError} method when the
     * subscriber subscribes to it.
     * <p>
     * <img width="640" height="190" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.error.png" alt="">
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code error} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param exception
     *            the particular Throwable to pass to {@link SingleObserver#onError onError}
     * @param <T>
     *            the type of the item (ostensibly) emitted by the Single
     * @return a Single that invokes the subscriber's {@link SingleObserver#onError onError} method when
     *         the subscriber subscribes to it
     * @see <a href="http://reactivex.io/documentation/operators/empty-never-throw.html">ReactiveX operators documentation: Throw</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Single<T> error(final Throwable exception) {
        ObjectHelper.requireNonNull(exception, "error is null");
        return error(Functions.justCallable(exception));
    }

    /**
     * Returns a {@link Single} that invokes passed function and emits its result for each new SingleObserver that subscribes.
     * <p>
     * Allows you to defer execution of passed function until SingleObserver subscribes to the {@link Single}.
     * It makes passed function "lazy".
     * Result of the function invocation will be emitted by the {@link Single}.
     * <dl>
     *   <dt><b>Scheduler:</b></dt>
     *   <dd>{@code fromCallable} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param callable
     *         function which execution should be deferred, it will be invoked when SingleObserver will subscribe to the {@link Single}.
     * @param <T>
     *         the type of the item emitted by the {@link Single}.
     * @return a {@link Single} whose {@link SingleObserver}s' subscriptions trigger an invocation of the given function.
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Single<T> fromCallable(final Callable<? extends T> callable) {
        ObjectHelper.requireNonNull(callable, "callable is null");
        return RxJavaPlugins.onAssembly(new SingleFromCallable<T>(callable));
    }

    /**
     * Converts a {@link Future} into a {@code Single}.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.from.Future.png" alt="">
     * <p>
     * You can convert any object that supports the {@link Future} interface into a Single that emits the return
     * value of the {@link Future#get} method of that object, by passing the object into the {@code from}
     * method.
     * <p>
     * <em>Important note:</em> This Single is blocking; you cannot unsubscribe from it.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code fromFuture} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param future
     *            the source {@link Future}
     * @param <T>
     *            the type of object that the {@link Future} returns, and also the type of item to be emitted by
     *            the resulting {@code Single}
     * @return a {@code Single} that emits the item from the source {@link Future}
     * @see <a href="http://reactivex.io/documentation/operators/from.html">ReactiveX operators documentation: From</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Single<T> fromFuture(Future<? extends T> future) {
        return toSingle(Flowable.<T>fromFuture(future));
    }

    /**
     * Converts a {@link Future} into a {@code Single}, with a timeout on the Future.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.from.Future.png" alt="">
     * <p>
     * You can convert any object that supports the {@link Future} interface into a {@code Single} that emits
     * the return value of the {@link Future#get} method of that object, by passing the object into the
     * {@code from} method.
     * <p>
     * <em>Important note:</em> This {@code Single} is blocking; you cannot unsubscribe from it.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code fromFuture} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param future
     *            the source {@link Future}
     * @param timeout
     *            the maximum time to wait before calling {@code get}
     * @param unit
     *            the {@link TimeUnit} of the {@code timeout} argument
     * @param <T>
     *            the type of object that the {@link Future} returns, and also the type of item to be emitted by
     *            the resulting {@code Single}
     * @return a {@code Single} that emits the item from the source {@link Future}
     * @see <a href="http://reactivex.io/documentation/operators/from.html">ReactiveX operators documentation: From</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Single<T> fromFuture(Future<? extends T> future, long timeout, TimeUnit unit) {
        return toSingle(Flowable.<T>fromFuture(future, timeout, unit));
    }

    /**
     * Converts a {@link Future} into a {@code Single}, with a timeout on the Future.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.from.Future.png" alt="">
     * <p>
     * You can convert any object that supports the {@link Future} interface into a {@code Single} that emits
     * the return value of the {@link Future#get} method of that object, by passing the object into the
     * {@code from} method.
     * <p>
     * <em>Important note:</em> This {@code Single} is blocking; you cannot unsubscribe from it.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>You specify the {@link Scheduler} where the blocking wait will happen.</dd>
     * </dl>
     *
     * @param future
     *            the source {@link Future}
     * @param timeout
     *            the maximum time to wait before calling {@code get}
     * @param unit
     *            the {@link TimeUnit} of the {@code timeout} argument
     * @param scheduler
     *            the Scheduler to use for the blocking wait
     * @param <T>
     *            the type of object that the {@link Future} returns, and also the type of item to be emitted by
     *            the resulting {@code Single}
     * @return a {@code Single} that emits the item from the source {@link Future}
     * @see <a href="http://reactivex.io/documentation/operators/from.html">ReactiveX operators documentation: From</a>
     */
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public static <T> Single<T> fromFuture(Future<? extends T> future, long timeout, TimeUnit unit, Scheduler scheduler) {
        return toSingle(Flowable.<T>fromFuture(future, timeout, unit, scheduler));
    }

    /**
     * Converts a {@link Future}, operating on a specified {@link Scheduler}, into a {@code Single}.
     * <p>
     * <img width="640" height="315" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.from.Future.s.png" alt="">
     * <p>
     * You can convert any object that supports the {@link Future} interface into a {@code Single} that emits
     * the return value of the {@link Future#get} method of that object, by passing the object into the
     * {@code from} method.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>You specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     *
     * @param future
     *            the source {@link Future}
     * @param scheduler
     *            the {@link Scheduler} to wait for the Future on. Use a Scheduler such as
     *            {@link Schedulers#io()} that can block and wait on the Future
     * @param <T>
     *            the type of object that the {@link Future} returns, and also the type of item to be emitted by
     *            the resulting {@code Single}
     * @return a {@code Single} that emits the item from the source {@link Future}
     * @see <a href="http://reactivex.io/documentation/operators/from.html">ReactiveX operators documentation: From</a>
     */
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public static <T> Single<T> fromFuture(Future<? extends T> future, Scheduler scheduler) {
        return toSingle(Flowable.<T>fromFuture(future, scheduler));
    }

    /**
     * Wraps a specific Publisher into a Single and signals its single element or error.
     * <p>If the source Publisher is empty, a NoSuchElementException is signalled. If
     * the source has more than one element, an IndexOutOfBoundsException is signalled.
     * <dl>
     * <dt><b>Backpressure:</b></dt>
     * <dd>The {@code publisher} is consumed in an unbounded fashion but will be cancelled
     * if it produced more than one item.</dd>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code fromPublisher} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <T> the value type
     * @param publisher the source Publisher instance, not null
     * @return the new Single instance
     */
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Single<T> fromPublisher(final Publisher<? extends T> publisher) {
        ObjectHelper.requireNonNull(publisher, "publisher is null");
        return RxJavaPlugins.onAssembly(new SingleFromPublisher<T>(publisher));
    }

    /**
     * Wraps a specific ObservableSource into a Single and signals its single element or error.
     * <p>If the ObservableSource is empty, a NoSuchElementException is signalled.
     * If the source has more than one element, an IndexOutOfBoundsException is signalled.
     * <p>
     * <dl>
     *   <dt><b>Scheduler:</b></dt>
     *   <dd>{@code fromObservable} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param observableSource the source Observable, not null
     * @param <T>
     *         the type of the item emitted by the {@link Single}.
     * @return the new Single instance
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Single<T> fromObservable(ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "observableSource is null");
        return RxJavaPlugins.onAssembly(new ObservableSingleSingle<T>(observableSource, null));
    }

    /**
     * Returns a {@code Single} that emits a specified item.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.just.png" alt="">
     * <p>
     * To convert any object into a {@code Single} that emits that object, pass that object into the
     * {@code just} method.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code just} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param item
     *            the item to emit
     * @param <T>
     *            the type of that item
     * @return a {@code Single} that emits {@code item}
     * @see <a href="http://reactivex.io/documentation/operators/just.html">ReactiveX operators documentation: Just</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Single<T> just(final T item) {
        ObjectHelper.requireNonNull(item, "value is null");
        return RxJavaPlugins.onAssembly(new SingleJust<T>(item));
    }

    /**
     * Merges an Iterable sequence of SingleSource instances into a single Flowable sequence,
     * running all SingleSources at once.
     * <dl>
     *  <dt><b>Backpressure:</b><dt>
     *  <dd>The returned {@code Flowable} honors the backpressure of the downstream consumer.</dd>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <T> the common and resulting value type
     * @param sources the Iterable sequence of SingleSource sources
     * @return the new Flowable instance
     * @since 2.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Flowable<T> merge(Iterable<? extends SingleSource<? extends T>> sources) {
        return merge(Flowable.fromIterable(sources));
    }

    /**
     * Merges a Flowable sequence of SingleSource instances into a single Flowable sequence,
     * running all SingleSources at once.
     * <dl>
     *  <dt><b>Backpressure:</b><dt>
     *  <dd>The returned {@code Flowable} honors the backpressure of the downstream consumer.</dd>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <T> the common and resulting value type
     * @param sources the Flowable sequence of SingleSource sources
     * @return the new Flowable instance
     * @since 2.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> Flowable<T> merge(Publisher<? extends SingleSource<? extends T>> sources) {
        return RxJavaPlugins.onAssembly(new FlowableFlatMap(sources, SingleInternalHelper.toFlowable(), false, Integer.MAX_VALUE, Flowable.bufferSize()));
    }

    /**
     * Flattens a {@code Single} that emits a {@code Single} into a single {@code Single} that emits the item
     * emitted by the nested {@code Single}, without any transformation.
     * <p>
     * <img width="640" height="370" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.merge.oo.png" alt="">
     * <p>
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <T> the value type of the sources and the output
     * @param source
     *            a {@code Single} that emits a {@code Single}
     * @return a {@code Single} that emits the item that is the result of flattening the {@code Single} emitted
     *         by {@code source}
     * @see <a href="http://reactivex.io/documentation/operators/merge.html">ReactiveX operators documentation: Merge</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> Single<T> merge(SingleSource<? extends SingleSource<? extends T>> source) {
        ObjectHelper.requireNonNull(source, "source is null");
        return RxJavaPlugins.onAssembly(new SingleFlatMap<SingleSource<? extends T>, T>(source, (Function)Functions.identity()));
    }

    /**
     * Flattens two Singles into a single Flowable, without any transformation.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.merge.png" alt="">
     * <p>
     * You can combine items emitted by multiple Singles so that they appear as a single Flowable, by
     * using the {@code merge} method.
     * <dl>
     *  <dt><b>Backpressure:</b><dt>
     *  <dd>The returned {@code Flowable} honors the backpressure of the downstream consumer.</dd>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <T> the common value type
     * @param source1
     *            a Single to be merged
     * @param source2
     *            a Single to be merged
     * @return a Flowable that emits all of the items emitted by the source Singles
     * @see <a href="http://reactivex.io/documentation/operators/merge.html">ReactiveX operators documentation: Merge</a>
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings("unchecked")
    public static <T> Flowable<T> merge(
            SingleSource<? extends T> source1, SingleSource<? extends T> source2
     ) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return merge(Flowable.fromArray(source1, source2));
    }

    /**
     * Flattens three Singles into a single Flowable, without any transformation.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.merge.png" alt="">
     * <p>
     * You can combine items emitted by multiple Singles so that they appear as a single Flowable, by using
     * the {@code merge} method.
     * <dl>
     *  <dt><b>Backpressure:</b><dt>
     *  <dd>The returned {@code Flowable} honors the backpressure of the downstream consumer.</dd>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <T> the common value type
     * @param source1
     *            a Single to be merged
     * @param source2
     *            a Single to be merged
     * @param source3
     *            a Single to be merged
     * @return a Flowable that emits all of the items emitted by the source Singles
     * @see <a href="http://reactivex.io/documentation/operators/merge.html">ReactiveX operators documentation: Merge</a>
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings("unchecked")
    public static <T> Flowable<T> merge(
            SingleSource<? extends T> source1, SingleSource<? extends T> source2,
            SingleSource<? extends T> source3
     ) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return merge(Flowable.fromArray(source1, source2, source3));
    }

    /**
     * Flattens four Singles into a single Flowable, without any transformation.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.merge.png" alt="">
     * <p>
     * You can combine items emitted by multiple Singles so that they appear as a single Flowable, by using
     * the {@code merge} method.
     * <dl>
     *  <dt><b>Backpressure:</b><dt>
     *  <dd>The returned {@code Flowable} honors the backpressure of the downstream consumer.</dd>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code merge} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <T> the common value type
     * @param source1
     *            a Single to be merged
     * @param source2
     *            a Single to be merged
     * @param source3
     *            a Single to be merged
     * @param source4
     *            a Single to be merged
     * @return a Flowable that emits all of the items emitted by the source Singles
     * @see <a href="http://reactivex.io/documentation/operators/merge.html">ReactiveX operators documentation: Merge</a>
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings("unchecked")
    public static <T> Flowable<T> merge(
            SingleSource<? extends T> source1, SingleSource<? extends T> source2,
            SingleSource<? extends T> source3, SingleSource<? extends T> source4
     ) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return merge(Flowable.fromArray(source1, source2, source3, source4));
    }

    /**
     * Returns a singleton instance of a never-signalling Single (only calls onSubscribe).
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code never} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <T> the target value type
     * @return the singleton never instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings("unchecked")
    public static <T> Single<T> never() {
        return RxJavaPlugins.onAssembly((Single<T>) SingleNever.INSTANCE);
    }

    /**
     * Signals success with 0L value after the given delay for each SingleObserver.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code timer} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * @param delay the delay amount
     * @param unit the time unit of the delay
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public static Single<Long> timer(long delay, TimeUnit unit) {
        return timer(delay, unit, Schedulers.computation());
    }

    /**
     * Signals success with 0L value after the given delay for each SingleObserver.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>you specify the {@link Scheduler} to signal on.</dd>
     * </dl>
     * @param delay the delay amount
     * @param unit the time unit of the delay
     * @param scheduler the scheduler where the single 0L will be emitted
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public static Single<Long> timer(final long delay, final TimeUnit unit, final Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new SingleTimer(delay, unit, scheduler));
    }

    /**
     * Compares two SingleSources and emits true if they emit the same value (compared via Object.equals).
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code equals} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <T> the common value type
     * @param first the first SingleSource instance
     * @param second the second SingleSource instance
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Single<Boolean> equals(final SingleSource<? extends T> first, final SingleSource<? extends T> second) { // NOPMD
        ObjectHelper.requireNonNull(first, "first is null");
        ObjectHelper.requireNonNull(second, "second is null");
        return RxJavaPlugins.onAssembly(new SingleEquals<T>(first, second));
    }

    /**
     * <strong>Advanced use only:</strong> creates a Single instance without
     * any safeguards by using a callback that is called with a SingleObserver.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code unsafeCreate} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <T> the value type
     * @param onSubscribe the function that is called with the subscribing SingleObserver
     * @return the new Single instance
     * @throws IllegalArgumentException if {@code source} is a subclass of {@code Single}; such
     * instances don't need conversion and is possibly a port remnant from 1.x or one should use {@link #hide()}
     * instead.
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Single<T> unsafeCreate(SingleSource<T> onSubscribe) {
        ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
        if (onSubscribe instanceof Single) {
            throw new IllegalArgumentException("unsafeCreate(Single) should be upgraded");
        }
        return RxJavaPlugins.onAssembly(new SingleFromUnsafeSource<T>(onSubscribe));
    }

    /**
     * Allows using and disposing a resource while running a SingleSource instance generated from
     * that resource (similar to a try-with-resources).
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code using} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <T> the value type of the SingleSource generated
     * @param <U> the resource type
     * @param resourceSupplier the Callable called for each SingleObserver to generate a resource Object
     * @param singleFunction the function called with the returned resource
     *                  Object from {@code resourceSupplier} and should return a SingleSource instance
     *                  to be run by the operator
     * @param disposer the consumer of the generated resource that is called exactly once for
     *                  that particular resource when the generated SingleSource terminates
     *                  (successfully or with an error) or gets cancelled.
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T, U> Single<T> using(Callable<U> resourceSupplier,
                                         Function<? super U, ? extends SingleSource<? extends T>> singleFunction,
                                         Consumer<? super U> disposer) {
        return using(resourceSupplier, singleFunction, disposer, true);
    }

    /**
     * Allows using and disposing a resource while running a SingleSource instance generated from
     * that resource (similar to a try-with-resources).
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code using} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <T> the value type of the SingleSource generated
     * @param <U> the resource type
     * @param resourceSupplier the Callable called for each SingleObserver to generate a resource Object
     * @param singleFunction the function called with the returned resource
     *                  Object from {@code resourceSupplier} and should return a SingleSource instance
     *                  to be run by the operator
     * @param disposer the consumer of the generated resource that is called exactly once for
     *                  that particular resource when the generated SingleSource terminates
     *                  (successfully or with an error) or gets cancelled.
     * @param eager
     *                 if true, the disposer is called before the terminal event is signalled
     *                 if false, the disposer is called after the terminal event is delivered to downstream
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T, U> Single<T> using(
            final Callable<U> resourceSupplier,
            final Function<? super U, ? extends SingleSource<? extends T>> singleFunction,
            final Consumer<? super U> disposer,
            final boolean eager) {
        ObjectHelper.requireNonNull(resourceSupplier, "resourceSupplier is null");
        ObjectHelper.requireNonNull(singleFunction, "singleFunction is null");
        ObjectHelper.requireNonNull(disposer, "disposer is null");

        return RxJavaPlugins.onAssembly(new SingleUsing<T, U>(resourceSupplier, singleFunction, disposer, eager));
    }

    /**
     * Wraps a SingleSource instance into a new Single instance if not already a Single
     * instance.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code wrap} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <T> the value type
     * @param source the source to wrap
     * @return the Single wrapper or the source cast to Single (if possible)
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Single<T> wrap(SingleSource<T> source) {
        ObjectHelper.requireNonNull(source, "source is null");
        if (source instanceof Single) {
            return RxJavaPlugins.onAssembly((Single<T>)source);
        }
        return RxJavaPlugins.onAssembly(new SingleFromUnsafeSource<T>(source));
    }

    /**
     * Waits until all SingleSource sources provided by the Iterable sequence signal a success
     * value and calls a zipper function with an array of these values to return a result
     * to be emitted to downstream.
     * <p>
     * Note on method signature: since Java doesn't allow creating a generic array with {@code new T[]}, the
     * implementation of this operator has to create an {@code Object[]} instead. Unfortunately, a
     * {@code Function<Integer[], R>} passed to the method would trigger a {@code ClassCastException}.
     *
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/zip.png" alt="">
     * <p>
     * If any of the SingleSources signal an error, all other SingleSources get cancelled and the
     * error emitted to downstream immediately.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code zip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <T> the common value type
     * @param <R> the result value type
     * @param sources the Iterable sequence of SingleSource instances
     * @param zipper the function that receives an array with values from each SingleSource
     *               and should return a value to be emitted to downstream
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T, R> Single<R> zip(final Iterable<? extends SingleSource<? extends T>> sources, Function<? super Object[], ? extends R> zipper) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return toSingle(Flowable.zipIterable(SingleInternalHelper.iterableToFlowable(sources), zipper, false, 1));
    }

    /**
     * Returns a Single that emits the results of a specified combiner function applied to two items emitted by
     * two other Singles.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.zip.png" alt="">
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code zip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <T1> the first source Single's value type
     * @param <T2> the second source Single's value type
     * @param <R> the result value type
     * @param source1
     *            the first source Single
     * @param source2
     *            a second source Single
     * @param zipper
     *            a function that, when applied to the item emitted by each of the source Singles, results in an
     *            item that will be emitted by the resulting Single
     * @return a Single that emits the zipped results
     * @see <a href="http://reactivex.io/documentation/operators/zip.html">ReactiveX operators documentation: Zip</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings("unchecked")
    public static <T1, T2, R> Single<R> zip(
            SingleSource<? extends T1> source1, SingleSource<? extends T2> source2,
            BiFunction<? super T1, ? super T2, ? extends R> zipper
     ) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2);
    }

    /**
     * Returns a Single that emits the results of a specified combiner function applied to three items emitted
     * by three other Singles.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.zip.png" alt="">
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code zip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <T1> the first source Single's value type
     * @param <T2> the second source Single's value type
     * @param <T3> the third source Single's value type
     * @param <R> the result value type
     * @param source1
     *            the first source Single
     * @param source2
     *            a second source Single
     * @param source3
     *            a third source Single
     * @param zipper
     *            a function that, when applied to the item emitted by each of the source Singles, results in an
     *            item that will be emitted by the resulting Single
     * @return a Single that emits the zipped results
     * @see <a href="http://reactivex.io/documentation/operators/zip.html">ReactiveX operators documentation: Zip</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, R> Single<R> zip(
            SingleSource<? extends T1> source1, SingleSource<? extends T2> source2,
            SingleSource<? extends T3> source3,
            Function3<? super T1, ? super T2, ? super T3, ? extends R> zipper
     ) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3);
    }

    /**
     * Returns a Single that emits the results of a specified combiner function applied to four items
     * emitted by four other Singles.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.zip.png" alt="">
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code zip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <T1> the first source Single's value type
     * @param <T2> the second source Single's value type
     * @param <T3> the third source Single's value type
     * @param <T4> the fourth source Single's value type
     * @param <R> the result value type
     * @param source1
     *            the first source Single
     * @param source2
     *            a second source Single
     * @param source3
     *            a third source Single
     * @param source4
     *            a fourth source Single
     * @param zipper
     *            a function that, when applied to the item emitted by each of the source Singles, results in an
     *            item that will be emitted by the resulting Single
     * @return a Single that emits the zipped results
     * @see <a href="http://reactivex.io/documentation/operators/zip.html">ReactiveX operators documentation: Zip</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, R> Single<R> zip(
            SingleSource<? extends T1> source1, SingleSource<? extends T2> source2,
            SingleSource<? extends T3> source3, SingleSource<? extends T4> source4,
            Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> zipper
     ) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4);
    }

    /**
     * Returns a Single that emits the results of a specified combiner function applied to five items
     * emitted by five other Singles.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.zip.png" alt="">
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code zip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <T1> the first source Single's value type
     * @param <T2> the second source Single's value type
     * @param <T3> the third source Single's value type
     * @param <T4> the fourth source Single's value type
     * @param <T5> the fifth source Single's value type
     * @param <R> the result value type
     * @param source1
     *            the first source Single
     * @param source2
     *            a second source Single
     * @param source3
     *            a third source Single
     * @param source4
     *            a fourth source Single
     * @param source5
     *            a fifth source Single
     * @param zipper
     *            a function that, when applied to the item emitted by each of the source Singles, results in an
     *            item that will be emitted by the resulting Single
     * @return a Single that emits the zipped results
     * @see <a href="http://reactivex.io/documentation/operators/zip.html">ReactiveX operators documentation: Zip</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, R> Single<R> zip(
            SingleSource<? extends T1> source1, SingleSource<? extends T2> source2,
            SingleSource<? extends T3> source3, SingleSource<? extends T4> source4,
            SingleSource<? extends T5> source5,
            Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> zipper
     ) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4, source5);
    }

    /**
     * Returns a Single that emits the results of a specified combiner function applied to six items
     * emitted by six other Singles.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.zip.png" alt="">
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code zip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <T1> the first source Single's value type
     * @param <T2> the second source Single's value type
     * @param <T3> the third source Single's value type
     * @param <T4> the fourth source Single's value type
     * @param <T5> the fifth source Single's value type
     * @param <T6> the sixth source Single's value type
     * @param <R> the result value type
     * @param source1
     *            the first source Single
     * @param source2
     *            a second source Single
     * @param source3
     *            a third source Single
     * @param source4
     *            a fourth source Single
     * @param source5
     *            a fifth source Single
     * @param source6
     *            a sixth source Single
     * @param zipper
     *            a function that, when applied to the item emitted by each of the source Singles, results in an
     *            item that will be emitted by the resulting Single
     * @return a Single that emits the zipped results
     * @see <a href="http://reactivex.io/documentation/operators/zip.html">ReactiveX operators documentation: Zip</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, R> Single<R> zip(
            SingleSource<? extends T1> source1, SingleSource<? extends T2> source2,
            SingleSource<? extends T3> source3, SingleSource<? extends T4> source4,
            SingleSource<? extends T5> source5, SingleSource<? extends T6> source6,
            Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> zipper
     ) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4, source5, source6);
    }

    /**
     * Returns a Single that emits the results of a specified combiner function applied to seven items
     * emitted by seven other Singles.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.zip.png" alt="">
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code zip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <T1> the first source Single's value type
     * @param <T2> the second source Single's value type
     * @param <T3> the third source Single's value type
     * @param <T4> the fourth source Single's value type
     * @param <T5> the fifth source Single's value type
     * @param <T6> the sixth source Single's value type
     * @param <T7> the seventh source Single's value type
     * @param <R> the result value type
     * @param source1
     *            the first source Single
     * @param source2
     *            a second source Single
     * @param source3
     *            a third source Single
     * @param source4
     *            a fourth source Single
     * @param source5
     *            a fifth source Single
     * @param source6
     *            a sixth source Single
     * @param source7
     *            a seventh source Single
     * @param zipper
     *            a function that, when applied to the item emitted by each of the source Singles, results in an
     *            item that will be emitted by the resulting Single
     * @return a Single that emits the zipped results
     * @see <a href="http://reactivex.io/documentation/operators/zip.html">ReactiveX operators documentation: Zip</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T7, R> Single<R> zip(
            SingleSource<? extends T1> source1, SingleSource<? extends T2> source2,
            SingleSource<? extends T3> source3, SingleSource<? extends T4> source4,
            SingleSource<? extends T5> source5, SingleSource<? extends T6> source6,
            SingleSource<? extends T7> source7,
            Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> zipper
     ) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        ObjectHelper.requireNonNull(source7, "source7 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4, source5, source6, source7);
    }

    /**
     * Returns a Single that emits the results of a specified combiner function applied to eight items
     * emitted by eight other Singles.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.zip.png" alt="">
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code zip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <T1> the first source Single's value type
     * @param <T2> the second source Single's value type
     * @param <T3> the third source Single's value type
     * @param <T4> the fourth source Single's value type
     * @param <T5> the fifth source Single's value type
     * @param <T6> the sixth source Single's value type
     * @param <T7> the seventh source Single's value type
     * @param <T8> the eighth source Single's value type
     * @param <R> the result value type
     * @param source1
     *            the first source Single
     * @param source2
     *            a second source Single
     * @param source3
     *            a third source Single
     * @param source4
     *            a fourth source Single
     * @param source5
     *            a fifth source Single
     * @param source6
     *            a sixth source Single
     * @param source7
     *            a seventh source Single
     * @param source8
     *            an eighth source Single
     * @param zipper
     *            a function that, when applied to the item emitted by each of the source Singles, results in an
     *            item that will be emitted by the resulting Single
     * @return a Single that emits the zipped results
     * @see <a href="http://reactivex.io/documentation/operators/zip.html">ReactiveX operators documentation: Zip</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Single<R> zip(
            SingleSource<? extends T1> source1, SingleSource<? extends T2> source2,
            SingleSource<? extends T3> source3, SingleSource<? extends T4> source4,
            SingleSource<? extends T5> source5, SingleSource<? extends T6> source6,
            SingleSource<? extends T7> source7, SingleSource<? extends T8> source8,
            Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> zipper
     ) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        ObjectHelper.requireNonNull(source7, "source7 is null");
        ObjectHelper.requireNonNull(source8, "source8 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4, source5, source6, source7, source8);
    }

    /**
     * Returns a Single that emits the results of a specified combiner function applied to nine items
     * emitted by nine other Singles.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.zip.png" alt="">
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code zip} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <T1> the first source Single's value type
     * @param <T2> the second source Single's value type
     * @param <T3> the third source Single's value type
     * @param <T4> the fourth source Single's value type
     * @param <T5> the fifth source Single's value type
     * @param <T6> the sixth source Single's value type
     * @param <T7> the seventh source Single's value type
     * @param <T8> the eighth source Single's value type
     * @param <T9> the ninth source Single's value type
     * @param <R> the result value type
     * @param source1
     *            the first source Single
     * @param source2
     *            a second source Single
     * @param source3
     *            a third source Single
     * @param source4
     *            a fourth source Single
     * @param source5
     *            a fifth source Single
     * @param source6
     *            a sixth source Single
     * @param source7
     *            a seventh source Single
     * @param source8
     *            an eighth source Single
     * @param source9
     *            a ninth source Single
     * @param zipper
     *            a function that, when applied to the item emitted by each of the source Singles, results in an
     *            item that will be emitted by the resulting Single
     * @return a Single that emits the zipped results
     * @see <a href="http://reactivex.io/documentation/operators/zip.html">ReactiveX operators documentation: Zip</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Single<R> zip(
            SingleSource<? extends T1> source1, SingleSource<? extends T2> source2,
            SingleSource<? extends T3> source3, SingleSource<? extends T4> source4,
            SingleSource<? extends T5> source5, SingleSource<? extends T6> source6,
            SingleSource<? extends T7> source7, SingleSource<? extends T8> source8,
            SingleSource<? extends T9> source9,
            Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipper
     ) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        ObjectHelper.requireNonNull(source7, "source7 is null");
        ObjectHelper.requireNonNull(source8, "source8 is null");
        ObjectHelper.requireNonNull(source9, "source9 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4, source5, source6, source7, source8, source9);
    }

    /**
     * Waits until all SingleSource sources provided via an array signal a success
     * value and calls a zipper function with an array of these values to return a result
     * to be emitted to downstream.
     * <p>
     * Note on method signature: since Java doesn't allow creating a generic array with {@code new T[]}, the
     * implementation of this operator has to create an {@code Object[]} instead. Unfortunately, a
     * {@code Function<Integer[], R>} passed to the method would trigger a {@code ClassCastException}.
     *
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/zip.png" alt="">
     * <p>
     * If any of the SingleSources signal an error, all other SingleSources get cancelled and the
     * error emitted to downstream immediately.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code zipArray} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <T> the common value type
     * @param <R> the result value type
     * @param sources the array of SingleSource instances
     * @param zipper the function that receives an array with values from each SingleSource
     *               and should return a value to be emitted to downstream
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T, R> Single<R> zipArray(Function<? super Object[], ? extends R> zipper, SingleSource<? extends T>... sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        Publisher[] sourcePublishers = new Publisher[sources.length];
        int i = 0;
        for (SingleSource<? extends T> s : sources) {
            ObjectHelper.requireNonNull(s, "The " + i + "th source is null");
            sourcePublishers[i] = RxJavaPlugins.onAssembly(new SingleToFlowable<T>(s));
            i++;
        }
        return toSingle(Flowable.zipArray(zipper, false, 1, sourcePublishers));
    }

    /**
     * Signals the event of this or the other SingleSource whichever signals first.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code ambWith} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param other the other SingleSource to race for the first emission of success or error
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings("unchecked")
    public final Single<T> ambWith(SingleSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return ambArray(this, other);
    }

    /**
     * Hides the identity of the current Single, including the Disposable that is sent
     * to the downstream via {@code onSubscribe()}.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code hide} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> hide() {
        return RxJavaPlugins.onAssembly(new SingleHide<T>(this));
    }

    /**
     * Transform a Single by applying a particular Transformer function to it.
     * <p>
     * This method operates on the Single itself whereas {@link #lift} operates on the Single's SingleObservers.
     * <p>
     * If the operator you are creating is designed to act on the individual item emitted by a Single, use
     * {@link #lift}. If your operator is designed to transform the source Single as a whole (for instance, by
     * applying a particular set of existing RxJava operators to it) use {@code compose}.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code compose} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <R> the value type of the single returned by the transformer function
     * @param transformer the transformer function, not null
     * @return the source Single, transformed by the transformer function
     * @see <a href="https://github.com/ReactiveX/RxJava/wiki/Implementing-Your-Own-Operators">RxJava wiki: Implementing Your Own Operators</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Single<R> compose(SingleTransformer<T, R> transformer) {
        return wrap(transformer.apply(this));
    }

    /**
     * Stores the success value or exception from the current Single and replays it to late SingleObservers.
     * <p>
     * The returned Single subscribes to the current Single when the first SingleObserver subscribes.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code cache} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> cache() {
        return RxJavaPlugins.onAssembly(new SingleCache<T>(this));
    }

    /**
     * Casts the success value of the current Single into the target type or signals a
     * ClassCastException if not compatible.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code cast} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <U> the target type
     * @param clazz the type token to use for casting the success result from the current Single
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U> Single<U> cast(final Class<? extends U> clazz) {
        ObjectHelper.requireNonNull(clazz, "clazz is null");
        return map(Functions.castFunction(clazz));
    }

    /**
     * Returns a Flowable that emits the item emitted by the source Single, then the item emitted by the
     * specified Single.
     * <p>
     * <img width="640" height="335" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.concatWith.png" alt="">
     * <dl>
     *  <dt><b>Backpressure:</b><dt>
     *  <dd>The returned {@code Flowable} honors the backpressure of the downstream consumer.</dd>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code concatWith} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param other
     *            a Single to be concatenated after the current
     * @return a Flowable that emits the item emitted by the source Single, followed by the item emitted by
     *         {@code t1}
     * @see <a href="http://reactivex.io/documentation/operators/concat.html">ReactiveX operators documentation: Concat</a>
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Flowable<T> concatWith(SingleSource<? extends T> other) {
        return concat(this, other);
    }

    /**
     * Delays the emission of the success or error signal from the current Single by
     * the specified amount.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code delay} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     *
     * @param time the time amount to delay the signals
     * @param unit the time unit
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Single<T> delay(long time, TimeUnit unit) {
        return delay(time, unit, Schedulers.computation());
    }

    /**
     * Delays the emission of the success or error signal from the current Single by
     * the specified amount.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>you specify the {@link Scheduler} where the non-blocking wait and emission happens</dd>
     * </dl>
     *
     * @param time the time amount to delay the signals
     * @param unit the time unit
     * @param scheduler the target scheduler to use fro the non-blocking wait and emission
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Single<T> delay(final long time, final TimeUnit unit, final Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new SingleDelay<T>(this, time, unit, scheduler));
    }

    /**
     * Delays the actual subscription to the current Single until the given other CompletableSource
     * completes.
     * <p>If the delaying source signals an error, that error is re-emitted and no subscription
     * to the current Single happens.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code delaySubscription} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param other the CompletableSource that has to complete before the subscription to the
     *              current Single happens
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> delaySubscription(CompletableSource other) {
        return RxJavaPlugins.onAssembly(new SingleDelayWithCompletable<T>(this, other));
    }

    /**
     * Delays the actual subscription to the current Single until the given other SingleSource
     * signals success.
     * <p>If the delaying source signals an error, that error is re-emitted and no subscription
     * to the current Single happens.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code delaySubscription} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <U> the element type of the other source
     * @param other the SingleSource that has to complete before the subscription to the
     *              current Single happens
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U> Single<T> delaySubscription(SingleSource<U> other) {
        return RxJavaPlugins.onAssembly(new SingleDelayWithSingle<T, U>(this, other));
    }

    /**
     * Delays the actual subscription to the current Single until the given other ObservableSource
     * signals its first value or completes.
     * <p>If the delaying source signals an error, that error is re-emitted and no subscription
     * to the current Single happens.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code delaySubscription} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <U> the element type of the other source
     * @param other the ObservableSource that has to signal a value or complete before the
     *              subscription to the current Single happens
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U> Single<T> delaySubscription(ObservableSource<U> other) {
        return RxJavaPlugins.onAssembly(new SingleDelayWithObservable<T, U>(this, other));
    }

    /**
     * Delays the actual subscription to the current Single until the given other Publisher
     * signals its first value or completes.
     * <p>If the delaying source signals an error, that error is re-emitted and no subscription
     * to the current Single happens.
     * <p>The other source is consumed in an unbounded manner (requesting Long.MAX_VALUE from it).
     * <dl>
     * <dt><b>Backpressure:</b></dt>
     * <dd>The {@code other} publisher is consumed in an unbounded fashion but will be
     * cancelled after the first item it produced.</dd>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code delaySubscription} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <U> the element type of the other source
     * @param other the Publisher that has to signal a value or complete before the
     *              subscription to the current Single happens
     * @return the new Single instance
     * @since 2.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U> Single<T> delaySubscription(Publisher<U> other) {
        return RxJavaPlugins.onAssembly(new SingleDelayWithPublisher<T, U>(this, other));
    }

    /**
     * Delays the actual subscription to the current Single until the given time delay elapsed.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code delaySubscription} does by default subscribe to the current Single
     * on the {@code computation} {@link Scheduler} after the delay.</dd>
     * </dl>
     * @param <U> the element type of the other source
     * @param time the time amount to wait with the subscription
     * @param unit the time unit of the waiting
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final <U> Single<T> delaySubscription(long time, TimeUnit unit) {
        return delaySubscription(time, unit, Schedulers.computation());
    }

    /**
     * Delays the actual subscription to the current Single until the given time delay elapsed.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code delaySubscription} does by default subscribe to the current Single
     * on the {@link Scheduler} you provided, after the delay.</dd>
     * </dl>
     * @param <U> the element type of the other source
     * @param time the time amount to wait with the subscription
     * @param unit the time unit of the waiting
     * @param scheduler the scheduler to wait on and subscribe on to the current Single
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final <U> Single<T> delaySubscription(long time, TimeUnit unit, Scheduler scheduler) {
        return delaySubscription(Observable.timer(time, unit, scheduler));
    }

    /**
     * Calls the shared consumer with the Disposable sent through the onSubscribe for each
     * SingleObserver that subscribes to the current Single.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code doOnSubscribe} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param onSubscribe the consumer called with the Disposable sent via onSubscribe
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> doOnSubscribe(final Consumer<? super Disposable> onSubscribe) {
        ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
        return RxJavaPlugins.onAssembly(new SingleDoOnSubscribe<T>(this, onSubscribe));
    }

    /**
     * Calls the shared consumer with the success value sent via onSuccess for each
     * SingleObserver that subscribes to the current Single.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code doOnSuccess} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param onSuccess the consumer called with the success value of onSuccess
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> doOnSuccess(final Consumer<? super T> onSuccess) {
        ObjectHelper.requireNonNull(onSuccess, "onSuccess is null");
        return RxJavaPlugins.onAssembly(new SingleDoOnSuccess<T>(this, onSuccess));
    }

    /**
     * Calls the shared consumer with the error sent via onError or the value
     * via onSuccess for each SingleObserver that subscribes to the current Single.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code doOnEvent} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param onEvent the consumer called with the success value of onEvent
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> doOnEvent(final BiConsumer<? super T, ? super Throwable> onEvent) {
        ObjectHelper.requireNonNull(onEvent, "onEvent is null");
        return RxJavaPlugins.onAssembly(new SingleDoOnEvent<T>(this, onEvent));
    }

    /**
     * Calls the shared consumer with the error sent via onError for each
     * SingleObserver that subscribes to the current Single.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code doOnError} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param onError the consumer called with the success value of onError
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> doOnError(final Consumer<? super Throwable> onError) {
        ObjectHelper.requireNonNull(onError, "onError is null");
        return RxJavaPlugins.onAssembly(new SingleDoOnError<T>(this, onError));
    }

    /**
     * Calls the shared runnable if a SingleObserver subscribed to the current Single
     * disposes the common Disposable it received via onSubscribe.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code doOnDispose} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param onDispose the runnable called when the subscription is disposed
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> doOnDispose(final Action onDispose) {
        ObjectHelper.requireNonNull(onDispose, "onDispose is null");
        return RxJavaPlugins.onAssembly(new SingleDoOnDispose<T>(this, onDispose));
    }

    /**
     * Filters the success item of the Single via a predicate function and emitting it if the predicate
     * returns true, completing otherwise.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/filter.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code filter} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param predicate
     *            a function that evaluates the item emitted by the source Maybe, returning {@code true}
     *            if it passes the filter
     * @return a Maybe that emit the item emitted by the source Maybe that the filter
     *         evaluates as {@code true}
     * @see <a href="http://reactivex.io/documentation/operators/filter.html">ReactiveX operators documentation: Filter</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Maybe<T> filter(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly(new MaybeFilterSingle<T>(this, predicate));
    }

    /**
     * Returns a Single that is based on applying a specified function to the item emitted by the source Single,
     * where that function returns a SingleSource.
     * <p>
     * <img width="640" height="300" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.flatMap.png" alt="">
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code flatMap} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <R> the result value type
     * @param mapper
     *            a function that, when applied to the item emitted by the source Single, returns a SingleSource
     * @return the Single returned from {@code mapper} when applied to the item emitted by the source Single
     * @see <a href="http://reactivex.io/documentation/operators/flatmap.html">ReactiveX operators documentation: FlatMap</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Single<R> flatMap(Function<? super T, ? extends SingleSource<? extends R>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly(new SingleFlatMap<T, R>(this, mapper));
    }

    /**
     * Returns a Maybe that is based on applying a specified function to the item emitted by the source Single,
     * where that function returns a MaybeSource.
     * <p>
     * <img width="640" height="300" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.flatMapMaybe.png" alt="">
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code flatMapMaybe} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <R> the result value type
     * @param mapper
     *            a function that, when applied to the item emitted by the source Single, returns a MaybeSource
     * @return the Maybe returned from {@code mapper} when applied to the item emitted by the source Single
     * @see <a href="http://reactivex.io/documentation/operators/flatmap.html">ReactiveX operators documentation: FlatMap</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Maybe<R> flatMapMaybe(final Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly(new SingleFlatMapMaybe<T, R>(this, mapper));
    }

    /**
     * Returns a Flowable that emits items based on applying a specified function to the item emitted by the
     * source Single, where that function returns a Publisher.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.flatMapObservable.png" alt="">
     * <dl>
     *  <dt><b>Backpressure:</b><dt>
     *  <dd>The returned {@code Flowable} honors the backpressure of the downstream consumer
     *  and the {@code Publisher} returned by the mapper function is expected to honor it as well.</dd>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code flatMapPublisher} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <R> the result value type
     * @param mapper
     *            a function that, when applied to the item emitted by the source Single, returns an
     *            Flowable
     * @return the Flowable returned from {@code func} when applied to the item emitted by the source Single
     * @see <a href="http://reactivex.io/documentation/operators/flatmap.html">ReactiveX operators documentation: FlatMap</a>
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Flowable<R> flatMapPublisher(Function<? super T, ? extends Publisher<? extends R>> mapper) {
        return toFlowable().flatMap(mapper);
    }

    /**
     * Returns a Flowable that merges each item emitted by the source Single with the values in an
     * Iterable corresponding to that item that is generated by a selector.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/mergeMapIterable.png" alt="">
     * <dl>
     *  <dt><b>Backpressure:</b></dt>
     *  <dd>The operator honors backpressure from downstream.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code flattenAsFlowable} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <U>
     *            the type of item emitted by the resulting Iterable
     * @param mapper
     *            a function that returns an Iterable sequence of values for when given an item emitted by the
     *            source Single
     * @return the new Flowable instance
     * @see <a href="http://reactivex.io/documentation/operators/flatmap.html">ReactiveX operators documentation: FlatMap</a>
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U> Flowable<U> flattenAsFlowable(final Function<? super T, ? extends Iterable<? extends U>> mapper) {
        return new SingleFlatMapIterableFlowable<T, U>(this, mapper);
    }

    /**
     * Returns an Observable that maps a success value into an Iterable and emits its items.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/mergeMapIterable.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code flattenAsObservable} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <U>
     *            the type of item emitted by the resulting Iterable
     * @param mapper
     *            a function that returns an Iterable sequence of values for when given an item emitted by the
     *            source Single
     * @return the new Observable instance
     * @see <a href="http://reactivex.io/documentation/operators/flatmap.html">ReactiveX operators documentation: FlatMap</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U> Observable<U> flattenAsObservable(final Function<? super T, ? extends Iterable<? extends U>> mapper) {
        return new SingleFlatMapIterableObservable<T, U>(this, mapper);
    }

    /**
     * Returns an Observable that is based on applying a specified function to the item emitted by the source Single,
     * where that function returns a SingleSource.
     * <p>
     * <img width="640" height="300" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.flatMap.png" alt="">
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code flatMapObservable} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <R> the result value type
     * @param mapper
     *            a function that, when applied to the item emitted by the source Single, returns an ObservableSource
     * @return the Observable returned from {@code func} when applied to the item emitted by the source Single
     * @see <a href="http://reactivex.io/documentation/operators/flatmap.html">ReactiveX operators documentation: FlatMap</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Observable<R> flatMapObservable(Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
        return toObservable().flatMap(mapper);
    }

    /**
     * Returns a {@link Completable} that completes based on applying a specified function to the item emitted by the
     * source {@link Single}, where that function returns a {@link Completable}.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.flatMapCompletable.png" alt="">
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code flatMapCompletable} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param mapper
     *            a function that, when applied to the item emitted by the source Single, returns a
     *            Completable
     * @return the Completable returned from {@code func} when applied to the item emitted by the source Single
     * @see <a href="http://reactivex.io/documentation/operators/flatmap.html">ReactiveX operators documentation: FlatMap</a>
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Completable flatMapCompletable(final Function<? super T, ? extends Completable> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly(new SingleFlatMapCompletable<T>(this, mapper));
    }

    /**
     * Waits in a blocking fashion until the current Single signals a success value (which is returned) or
     * an exception (which is propagated).
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code blockingGet} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @return the success value
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final T blockingGet() {
        BlockingMultiObserver<T> observer = new BlockingMultiObserver<T>();
        subscribe(observer);
        return observer.blockingGet();
    }

    /**
     * Lifts a function to the current Single and returns a new Single that when subscribed to will pass the
     * values of the current Single through the Operator function.
     * <p>
     * In other words, this allows chaining TaskExecutors together on a Single for acting on the values within
     * the Single.
     * <p>
     * {@code task.map(...).filter(...).lift(new OperatorA()).lift(new OperatorB(...)).subscribe() }
     * <p>
     * If the operator you are creating is designed to act on the item emitted by a source Single, use
     * {@code lift}. If your operator is designed to transform the source Single as a whole (for instance, by
     * applying a particular set of existing RxJava operators to it) use {@link #compose}.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code lift} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <R> the downstream's value type (output)
     * @param lift
     *            the Operator that implements the Single-operating function to be applied to the source Single
     * @return a Single that is the result of applying the lifted Operator to the source Single
     * @see <a href="https://github.com/ReactiveX/RxJava/wiki/Implementing-Your-Own-Operators">RxJava wiki: Implementing Your Own Operators</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Single<R> lift(final SingleOperator<? extends R, ? super T> lift) {
        ObjectHelper.requireNonNull(lift, "onLift is null");
        return RxJavaPlugins.onAssembly(new SingleLift<T, R>(this, lift));
    }

    /**
     * Returns a Single that applies a specified function to the item emitted by the source Single and
     * emits the result of this function application.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.map.png" alt="">
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code map} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <R> the result value type
     * @param mapper
     *            a function to apply to the item emitted by the Single
     * @return a Single that emits the item from the source Single, transformed by the specified function
     * @see <a href="http://reactivex.io/documentation/operators/map.html">ReactiveX operators documentation: Map</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> Single<R> map(Function<? super T, ? extends R> mapper) {
        return RxJavaPlugins.onAssembly(new SingleMap<T, R>(this, mapper));
    }

    /**
     * Signals true if the current Single signals a success value that is Object-equals with the value
     * provided.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code contains} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param value the value to compare against the success value of this Single
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<Boolean> contains(Object value) {
        return contains(value, ObjectHelper.equalsPredicate());
    }

    /**
     * Signals true if the current Single signals a success value that is equal with
     * the value provided by calling a bi-predicate.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code contains} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param value the value to compare against the success value of this Single
     * @param comparer the function that receives the success value of this Single, the value provided
     *                 and should return true if they are considered equal
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<Boolean> contains(final Object value, final BiPredicate<Object, Object> comparer) {
        ObjectHelper.requireNonNull(value, "value is null");
        ObjectHelper.requireNonNull(comparer, "comparer is null");
        return RxJavaPlugins.onAssembly(new SingleContains<T>(this, value, comparer));
    }

    /**
     * Flattens this and another Single into a single Flowable, without any transformation.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.merge.png" alt="">
     * <p>
     * You can combine items emitted by multiple Singles so that they appear as a single Flowable, by using
     * the {@code mergeWith} method.
     * <dl>
     *  <dt><b>Backpressure:</b><dt>
     *  <dd>The returned {@code Flowable} honors the backpressure of the downstream consumer.</dd>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code mergeWith} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param other
     *            a Single to be merged
     * @return  that emits all of the items emitted by the source Singles
     * @see <a href="http://reactivex.io/documentation/operators/merge.html">ReactiveX operators documentation: Merge</a>
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Flowable<T> mergeWith(SingleSource<? extends T> other) {
        return merge(this, other);
    }

    /**
     * Modifies a Single to emit its item (or notify of its error) on a specified {@link Scheduler},
     * asynchronously.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.observeOn.png" alt="">
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     *
     * @param scheduler
     *            the {@link Scheduler} to notify subscribers on
     * @return the source Single modified so that its subscribers are notified on the specified
     *         {@link Scheduler}
     * @see <a href="http://reactivex.io/documentation/operators/observeon.html">ReactiveX operators documentation: ObserveOn</a>
     * @see <a href="http://www.grahamlea.com/2014/07/rxjava-threading-examples/">RxJava Threading Examples</a>
     * @see #subscribeOn
     */
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Single<T> observeOn(final Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new SingleObserveOn<T>(this, scheduler));
    }

    /**
     * Instructs a Single to emit an item (returned by a specified function) rather than invoking
     * {@link SingleObserver#onError onError} if it encounters an error.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.onErrorReturn.png" alt="">
     * <p>
     * By default, when a Single encounters an error that prevents it from emitting the expected item to its
     * subscriber, the Single invokes its subscriber's {@link SingleObserver#onError} method, and then quits
     * without invoking any more of its subscriber's methods. The {@code onErrorReturn} method changes this
     * behavior. If you pass a function ({@code resumeFunction}) to a Single's {@code onErrorReturn} method, if
     * the original Single encounters an error, instead of invoking its subscriber's
     * {@link SingleObserver#onError} method, it will instead emit the return value of {@code resumeFunction}.
     * <p>
     * You can use this to prevent errors from propagating or to supply fallback data should errors be
     * encountered.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code onErrorReturn} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param resumeFunction
     *            a function that returns an item that the new Single will emit if the source Single encounters
     *            an error
     * @return the original Single with appropriately modified behavior
     * @see <a href="http://reactivex.io/documentation/operators/catch.html">ReactiveX operators documentation: Catch</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> onErrorReturn(final Function<Throwable, ? extends T> resumeFunction) {
        ObjectHelper.requireNonNull(resumeFunction, "resumeFunction is null");
        return RxJavaPlugins.onAssembly(new SingleOnErrorReturn<T>(this, resumeFunction, null));
    }

    /**
     * Signals the specified value as success in case the current Single signals an error.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code onErrorReturnItem} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param value the value to signal if the current Single fails
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> onErrorReturnItem(final T value) {
        ObjectHelper.requireNonNull(value, "value is null");
        return RxJavaPlugins.onAssembly(new SingleOnErrorReturn<T>(this, null, value));
    }

    /**
     * Instructs a Single to pass control to another Single rather than invoking
     * {@link SingleObserver#onError(Throwable)} if it encounters an error.
     * <p/>
     * <img width="640" height="310" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/onErrorResumeNext.png" alt="">
     * <p/>
     * By default, when a Single encounters an error that prevents it from emitting the expected item to
     * its {@link SingleObserver}, the Single invokes its SingleObserver's {@code onError} method, and then quits
     * without invoking any more of its SingleObserver's methods. The {@code onErrorResumeNext} method changes this
     * behavior. If you pass another Single ({@code resumeSingleInCaseOfError}) to a Single's
     * {@code onErrorResumeNext} method, if the original Single encounters an error, instead of invoking its
     * SingleObserver's {@code onError} method, it will instead relinquish control to {@code resumeSingleInCaseOfError} which
     * will invoke the SingleObserver's {@link SingleObserver#onSuccess onSuccess} method if it is able to do so. In such a case,
     * because no Single necessarily invokes {@code onError}, the SingleObserver may never know that an error
     * happened.
     * <p/>
     * You can use this to prevent errors from propagating or to supply fallback data should errors be
     * encountered.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code onErrorResumeNext} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param resumeSingleInCaseOfError a Single that will take control if source Single encounters an error.
     * @return the original Single, with appropriately modified behavior.
     * @see <a href="http://reactivex.io/documentation/operators/catch.html">ReactiveX operators documentation: Catch</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> onErrorResumeNext(final Single<? extends T> resumeSingleInCaseOfError) {
        ObjectHelper.requireNonNull(resumeSingleInCaseOfError, "resumeSingleInCaseOfError is null");
        return onErrorResumeNext(Functions.justFunction(resumeSingleInCaseOfError));
    }

    /**
     * Instructs a Single to pass control to another Single rather than invoking
     * {@link SingleObserver#onError(Throwable)} if it encounters an error.
     * <p/>
     * <img width="640" height="310" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/onErrorResumeNext.png" alt="">
     * <p/>
     * By default, when a Single encounters an error that prevents it from emitting the expected item to
     * its {@link SingleObserver}, the Single invokes its SingleObserver's {@code onError} method, and then quits
     * without invoking any more of its SingleObserver's methods. The {@code onErrorResumeNext} method changes this
     * behavior. If you pass a function that will return another Single ({@code resumeFunctionInCaseOfError}) to a Single's
     * {@code onErrorResumeNext} method, if the original Single encounters an error, instead of invoking its
     * SingleObserver's {@code onError} method, it will instead relinquish control to {@code resumeSingleInCaseOfError} which
     * will invoke the SingleObserver's {@link SingleObserver#onSuccess onSuccess} method if it is able to do so. In such a case,
     * because no Single necessarily invokes {@code onError}, the SingleObserver may never know that an error
     * happened.
     * <p/>
     * You can use this to prevent errors from propagating or to supply fallback data should errors be
     * encountered.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code onErrorResumeNext} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param resumeFunctionInCaseOfError a function that returns a Single that will take control if source Single encounters an error.
     * @return the original Single, with appropriately modified behavior.
     * @see <a href="http://reactivex.io/documentation/operators/catch.html">ReactiveX operators documentation: Catch</a>
     * @since .20
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> onErrorResumeNext(
            final Function<? super Throwable, ? extends SingleSource<? extends T>> resumeFunctionInCaseOfError) {
        ObjectHelper.requireNonNull(resumeFunctionInCaseOfError, "resumeFunctionInCaseOfError is null");
        return RxJavaPlugins.onAssembly(new SingleResumeNext<T>(this, resumeFunctionInCaseOfError));
    }

    /**
     * Repeatedly re-subscribes to the current Single and emits each success value.
     * <dl>
     *  <dt><b>Backpressure:</b><dt>
     *  <dd>The returned {@code Flowable} honors the backpressure of the downstream consumer.</dd>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code repeat} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @return the new Flowable instance
     * @since 2.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Flowable<T> repeat() {
        return toFlowable().repeat();
    }

    /**
     * Re-subscribes to the current Single at most the given number of times and emits each success value.
     * <dl>
     *  <dt><b>Backpressure:</b><dt>
     *  <dd>The returned {@code Flowable} honors the backpressure of the downstream consumer.</dd>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code repeat} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param times the number of times to re-subscribe to the current Single
     * @return the new Flowable instance
     * @since 2.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Flowable<T> repeat(long times) {
        return toFlowable().repeat(times);
    }

    /**
     * Re-subscribes to the current Single if
     * the Publisher returned by the handler function signals a value in response to a
     * value signalled through the Flowable the handle receives.
     * <dl>
     *  <dt><b>Backpressure:</b><dt>
     *  <dd>The returned {@code Flowable} honors the backpressure of the downstream consumer.
     *  The {@code Publisher} returned by the handler function is expected to honor backpressure as well.</dd>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code repeatWhen} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param handler the function that is called with a Flowable that signals a value when the Single
     *                signalled a success value and returns a Publisher that has to signal a value to
     *                trigger a resubscription to the current Single, otherwise the terminal signal of
     *                the Publisher will be the terminal signal of the sequence as well.
     * @return the new Flowable instance
     * @since 2.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Flowable<T> repeatWhen(Function<? super Flowable<Object>, ? extends Publisher<Object>> handler) {
        return toFlowable().repeatWhen(handler);
    }

    /**
     * Re-subscribes to the current Single until the given BooleanSupplier returns true.
     * <dl>
     *  <dt><b>Backpressure:</b><dt>
     *  <dd>The returned {@code Flowable} honors the backpressure of the downstream consumer.</dd>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code repeatUntil} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param stop the BooleanSupplier called after the current Single succeeds and if returns false,
     *             the Single is re-subscribed; otherwise the sequence completes.
     * @return the new Flowable instance
     * @since 2.0
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Flowable<T> repeatUntil(BooleanSupplier stop) {
        return toFlowable().repeatUntil(stop);
    }

    /**
     * Repeatedly re-subscribes to the current Single indefinitely if it fails with an onError.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code retry} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> retry() {
        return toSingle(toFlowable().retry());
    }

    /**
     * Repeatedly re-subscribe at most the specified times to the current Single
     * if it fails with an onError.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code retry} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param times the number of times to resubscribe if the current Single fails
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> retry(long times) {
        return toSingle(toFlowable().retry(times));
    }

    /**
     * Re-subscribe to the current Single if the given predicate returns true when the Single fails
     * with an onError.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code retry} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param predicate the predicate called with the resubscription count and the failure Throwable
     *                  and should return true if a resubscription should happen
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> retry(BiPredicate<? super Integer, ? super Throwable> predicate) {
        return toSingle(toFlowable().retry(predicate));
    }

    /**
     * Re-subscribe to the current Single if the given predicate returns true when the Single fails
     * with an onError.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code retry} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param predicate the predicate called with the failure Throwable
     *                  and should return true if a resubscription should happen
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> retry(Predicate<? super Throwable> predicate) {
        return toSingle(toFlowable().retry(predicate));
    }

    /**
     * Re-subscribes to the current Single if and when the Publisher returned by the handler
     * function signals a value.
     * <p>
     * If the Publisher signals an onComplete, the resulting Single will signal a NoSuchElementException.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code retryWhen} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param handler the function that receives a Flowable of the error the Single emits and should
     *                return a Publisher that should signal a normal value (in response to the
     *                throwable the Flowable emits) to trigger a resubscription or signal an error to
     *                be the output of the resulting Single
     * @return the new Single instance
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> retryWhen(Function<? super Flowable<Throwable>, ? extends Publisher<Object>> handler) {
        return toSingle(toFlowable().retryWhen(handler));
    }

    /**
     * Subscribes to a Single but ignore its emission or notification.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code subscribe} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @return a {@link Disposable} reference can request the {@link Single} stop work.
     * @see <a href="http://reactivex.io/documentation/operators/subscribe.html">ReactiveX operators documentation: Subscribe</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Disposable subscribe() {
        return subscribe(Functions.emptyConsumer(), Functions.ERROR_CONSUMER);
    }

    /**
     * Subscribes to a Single and provides a composite callback to handle the item it emits
     * or any error notification it issues.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code subscribe} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param onCallback
     *            the callback that receives either the success value or the failure Throwable
     *            (whichever is not null)
     * @return a {@link Disposable} reference can request the {@link Single} stop work.
     * @see <a href="http://reactivex.io/documentation/operators/subscribe.html">ReactiveX operators documentation: Subscribe</a>
     * @throws IllegalArgumentException
     *             if {@code onCallback} is null
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Disposable subscribe(final BiConsumer<? super T, ? super Throwable> onCallback) {
        ObjectHelper.requireNonNull(onCallback, "onCallback is null");

        BiConsumerSingleObserver<T> s = new BiConsumerSingleObserver<T>(onCallback);
        subscribe(s);
        return s;
    }

    /**
     * Subscribes to a Single and provides a callback to handle the item it emits.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code subscribe} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param onSuccess
     *            the {@code Consumer<T>} you have designed to accept the emission from the Single
     * @return a {@link Disposable} reference can request the {@link Single} stop work.
     * @throws NullPointerException
     *             if {@code onSuccess} is null
     * @see <a href="http://reactivex.io/documentation/operators/subscribe.html">ReactiveX operators documentation: Subscribe</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Disposable subscribe(Consumer<? super T> onSuccess) {
        return subscribe(onSuccess, Functions.ERROR_CONSUMER);
    }

    /**
     * Subscribes to a Single and provides callbacks to handle the item it emits or any error notification it
     * issues.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code subscribe} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param onSuccess
     *            the {@code Consumer<T>} you have designed to accept the emission from the Single
     * @param onError
     *            the {@code Consumer<Throwable>} you have designed to accept any error notification from the
     *            Single
     * @return a {@link Disposable} reference can request the {@link Single} stop work.
     * @see <a href="http://reactivex.io/documentation/operators/subscribe.html">ReactiveX operators documentation: Subscribe</a>
     * @throws IllegalArgumentException
     *             if {@code onSuccess} is null, or
     *             if {@code onError} is null
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Disposable subscribe(final Consumer<? super T> onSuccess, final Consumer<? super Throwable> onError) {
        ObjectHelper.requireNonNull(onSuccess, "onSuccess is null");
        ObjectHelper.requireNonNull(onError, "onError is null");

        ConsumerSingleObserver<T> s = new ConsumerSingleObserver<T>(onSuccess, onError);
        subscribe(s);
        return s;
    }

    @SchedulerSupport(SchedulerSupport.NONE)
    @Override
    public final void subscribe(SingleObserver<? super T> subscriber) {
        ObjectHelper.requireNonNull(subscriber, "subscriber is null");

        subscriber = RxJavaPlugins.onSubscribe(this, subscriber);

        ObjectHelper.requireNonNull(subscriber, "subscriber returned by the RxJavaPlugins hook is null");

        try {
            subscribeActual(subscriber);
        } catch (NullPointerException ex) {
            throw ex;
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            NullPointerException npe = new NullPointerException("subscribeActual failed");
            npe.initCause(ex);
            throw npe;
        }
    }

    /**
     * Override this method in subclasses to handle the incoming SingleObservers.
     * @param observer the SingleObserver to handle, not null
     */
    protected abstract void subscribeActual(SingleObserver<? super T> observer);

    /**
     * Subscribes a given SingleObserver (subclass) to this Single and returns the given
     * SingleObserver as is.
     * <p>Usage example:
     * <pre><code>
     * Single&lt;Integer> source = Single.just(1);
     * CompositeDisposable composite = new CompositeDisposable();
     *
     * class ResourceSingleObserver implements SingleObserver&lt;Integer>, Disposable {
     *     // ...
     * }
     *
     * composite.add(source.subscribeWith(new ResourceSingleObserver()));
     * </code></pre>
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code subscribeWith} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <E> the type of the SingleObserver to use and return
     * @param observer the SingleObserver (subclass) to use and return, not null
     * @return the input {@code observer}
     * @throws NullPointerException if {@code observer} is null
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <E extends SingleObserver<? super T>> E subscribeWith(E observer) {
        subscribe(observer);
        return observer;
    }

    /**
     * Asynchronously subscribes subscribers to this Single on the specified {@link Scheduler}.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.subscribeOn.png" alt="">
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>You specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     *
     * @param scheduler
     *            the {@link Scheduler} to perform subscription actions on
     * @return the source Single modified so that its subscriptions happen on the specified {@link Scheduler}
     * @see <a href="http://reactivex.io/documentation/operators/subscribeon.html">ReactiveX operators documentation: SubscribeOn</a>
     * @see <a href="http://www.grahamlea.com/2014/07/rxjava-threading-examples/">RxJava Threading Examples</a>
     * @see #observeOn
     */
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Single<T> subscribeOn(final Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new SingleSubscribeOn<T>(this, scheduler));
    }

    /**
     * Returns a Single that emits the item emitted by the source Single until a Completable terminates. Upon
     * termination of {@code other}, this will emit a {@link CancellationException} rather than go to
     * {@link SingleObserver#onSuccess(Object)}.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/takeUntil.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code takeUntil} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param other
     *            the Completable whose termination will cause {@code takeUntil} to emit the item from the source
     *            Single
     * @return a Single that emits the item emitted by the source Single until such time as {@code other} terminates.
     * @see <a href="http://reactivex.io/documentation/operators/takeuntil.html">ReactiveX operators documentation: TakeUntil</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Single<T> takeUntil(final CompletableSource other) {
        return takeUntil(new CompletableToFlowable<T>(other));
    }

    /**
     * Returns a Single that emits the item emitted by the source Single until a Publisher emits an item. Upon
     * emission of an item from {@code other}, this will emit a {@link CancellationException} rather than go to
     * {@link SingleObserver#onSuccess(Object)}.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/takeUntil.png" alt="">
     * <dl>
     *  <dt><b>Backpressure:</b></dt>
     *  <dd>The {@code other} publisher is consumed in an unbounded fashion but will be
     *  cancelled after the first item it produced.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code takeUntil} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param other
     *            the Publisher whose first emitted item will cause {@code takeUntil} to emit the item from the source
     *            Single
     * @param <E>
     *            the type of items emitted by {@code other}
     * @return a Single that emits the item emitted by the source Single until such time as {@code other} emits
     * its first item
     * @see <a href="http://reactivex.io/documentation/operators/takeuntil.html">ReactiveX operators documentation: TakeUntil</a>
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <E> Single<T> takeUntil(final Publisher<E> other) {
        return RxJavaPlugins.onAssembly(new SingleTakeUntil<T, E>(this, other));
    }

    /**
     * Returns a Single that emits the item emitted by the source Single until a second Single emits an item. Upon
     * emission of an item from {@code other}, this will emit a {@link CancellationException} rather than go to
     * {@link SingleObserver#onSuccess(Object)}.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/takeUntil.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code takeUntil} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param other
     *            the Single whose emitted item will cause {@code takeUntil} to emit the item from the source Single
     * @param <E>
     *            the type of item emitted by {@code other}
     * @return a Single that emits the item emitted by the source Single until such time as {@code other} emits its item
     * @see <a href="http://reactivex.io/documentation/operators/takeuntil.html">ReactiveX operators documentation: TakeUntil</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <E> Single<T> takeUntil(final SingleSource<? extends E> other) {
        return takeUntil(new SingleToFlowable<E>(other));
    }

    /**
     * Signals a TimeoutException if the current Single doesn't signal a success value within the
     * specified timeout window.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code timeout} signals the TimeoutException on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * @param timeout the timeout amount
     * @param unit the time unit
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Single<T> timeout(long timeout, TimeUnit unit) {
        return timeout0(timeout, unit, Schedulers.computation(), null);
    }

    /**
     * Signals a TimeoutException if the current Single doesn't signal a success value within the
     * specified timeout window.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code timeout} signals the TimeoutException on the {@link Scheduler} you specify.</dd>
     * </dl>
     * @param timeout the timeout amount
     * @param unit the time unit
     * @param scheduler the target scheduler where the timeout is awaited and the TimeoutException
     *                  signalled
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Single<T> timeout(long timeout, TimeUnit unit, Scheduler scheduler) {
        return timeout0(timeout, unit, scheduler, null);
    }

    /**
     * Runs the current Single and if it doesn't signal within the specified timeout window, it is
     * cancelled and the other SingleSource subscribed to.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code timeout} subscribes to the other SingleSource on the {@link Scheduler} you specify.</dd>
     * </dl>
     * @param timeout the timeout amount
     * @param unit the time unit
     * @param scheduler the scheduler where the timeout is awaited and the subscription to other happens
     * @param other the other SingleSource that gets subscribed to if the current Single times out
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Single<T> timeout(long timeout, TimeUnit unit, Scheduler scheduler, SingleSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return timeout0(timeout, unit, scheduler, other);
    }

    /**
     * Runs the current Single and if it doesn't signal within the specified timeout window, it is
     * cancelled and the other SingleSource subscribed to.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code timeout} subscribes to the other SingleSource on
     *  the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     * @param timeout the timeout amount
     * @param unit the time unit
     * @param other the other SingleSource that gets subscribed to if the current Single times out
     * @return the new Single instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.COMPUTATION)
    public final Single<T> timeout(long timeout, TimeUnit unit, SingleSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return timeout0(timeout, unit, Schedulers.computation(), other);
    }

    private Single<T> timeout0(final long timeout, final TimeUnit unit, final Scheduler scheduler, final SingleSource<? extends T> other) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new SingleTimeout<T>(this, timeout, unit, scheduler, other));
    }

    /**
     * Calls the specified converter function with the current Single instance
     * during assembly time and returns its result.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code to} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param <R> the result type
     * @param convert the function that is called with the current Single instance during
     *                assembly time that should return some value to be the result
     *
     * @return the value returned by the convert function
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <R> R to(Function<? super Single<T>, R> convert) {
        try {
            return convert.apply(this);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    /**
     * Returns a {@link Completable} that discards result of the {@link Single}
     * and calls {@code onComplete} when this source {@link Single} calls
     * {@code onSuccess}. Error terminal event is propagated.
     * <p>
     * <img width="640" height="295" src=
     * "https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Completable.toCompletable.png"
     * alt="">
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code toCompletable} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @return a {@link Completable} that calls {@code onComplete} on it's subscriber when the source {@link Single}
     *         calls {@code onSuccess}.
     * @see <a href="http://reactivex.io/documentation/completable.html">ReactiveX documentation: Completable</a>
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Completable toCompletable() {
        return RxJavaPlugins.onAssembly(new CompletableFromSingle<T>(this));
    }

    /**
     * Converts this Single into a {@link Flowable}.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.toObservable.png" alt="">
     * <dl>
     *  <dt><b>Backpressure:</b><dt>
     *  <dd>The returned {@code Flowable} honors the backpressure of the downstream consumer.</dd>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code toFlowable} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @return a {@link Flowable} that emits a single item T or an error.
     */
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings("unchecked")
    public final Flowable<T> toFlowable() {
        if (this instanceof FuseToFlowable) {
            return ((FuseToFlowable<T>)this).fuseToFlowable();
        }
        return RxJavaPlugins.onAssembly(new SingleToFlowable<T>(this));
    }

    /**
     * Returns a {@link Future} representing the single value emitted by this {@code Single}.
     * <p>
     * <img width="640" height="395" src="https://github.com/ReactiveX/RxJava/wiki/images/rx-operators/B.toFuture.png" alt="">
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code toFuture} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @return a {@link Future} that expects a single item to be emitted by this {@code Single}
     * @see <a href="http://reactivex.io/documentation/operators/to.html">ReactiveX documentation: To</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Future<T> toFuture() {
        return subscribeWith(new FutureSingleObserver<T>());
    }

    /**
     * Converts this Single into a {@link Maybe}.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.toObservable.png" alt="">
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code toMaybe} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @return a {@link Maybe} that emits a single item T or an error.
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings("unchecked")
    public final Maybe<T> toMaybe() {
        if (this instanceof FuseToMaybe) {
            return ((FuseToMaybe<T>)this).fuseToMaybe();
        }
        return RxJavaPlugins.onAssembly(new MaybeFromSingle<T>(this));
    }
    /**
     * Converts this Single into an {@link Observable}.
     * <p>
     * <img width="640" height="305" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.toObservable.png" alt="">
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code toObservable} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @return an {@link Observable} that emits a single item T or an error.
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    @SuppressWarnings("unchecked")
    public final Observable<T> toObservable() {
        if (this instanceof FuseToObservable) {
            return ((FuseToObservable<T>)this).fuseToObservable();
        }
        return RxJavaPlugins.onAssembly(new SingleToObservable<T>(this));
    }

    /**
     * Returns a Single that emits the result of applying a specified function to the pair of items emitted by
     * the source Single and another specified Single.
     * <p>
     * <img width="640" height="380" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/Single.zip.png" alt="">
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code zipWith} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <U>
     *            the type of items emitted by the {@code other} Single
     * @param <R>
     *            the type of items emitted by the resulting Single
     * @param other
     *            the other SingleSource
     * @param zipper
     *            a function that combines the pairs of items from the two SingleSources to generate the items to
     *            be emitted by the resulting Single
     * @return a Single that pairs up values from the source Single and the {@code other} SingleSource
     *         and emits the results of {@code zipFunction} applied to these pairs
     * @see <a href="http://reactivex.io/documentation/operators/zip.html">ReactiveX operators documentation: Zip</a>
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final <U, R> Single<R> zipWith(SingleSource<U> other, BiFunction<? super T, ? super U, ? extends R> zipper) {
        return zip(this, other, zipper);
    }

    // -------------------------------------------------------------------------
    // Fluent test support, super handy and reduces test preparation boilerplate
    // -------------------------------------------------------------------------
    /**
     * Creates a TestObserver and subscribes
     * it to this Single.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code test} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @return the new TestObserver instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final TestObserver<T> test() {
        TestObserver<T> ts = new TestObserver<T>();
        subscribe(ts);
        return ts;
    }

    /**
     * Creates a TestObserver optionally in cancelled state, then subscribes it to this Single.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code test} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     * @param cancelled if true, the TestObserver will be cancelled before subscribing to this
     * Single.
     * @return the new TestObserver instance
     * @since 2.0
     */
    @SchedulerSupport(SchedulerSupport.NONE)
    public final TestObserver<T> test(boolean cancelled) {
        TestObserver<T> ts = new TestObserver<T>();

        if (cancelled) {
            ts.cancel();
        }

        subscribe(ts);
        return ts;
    }

    private static <T> Single<T> toSingle(Flowable<T> source) {
        return RxJavaPlugins.onAssembly(new FlowableSingleSingle<T>(source, null));
    }
}

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

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;

/**
 * Abstraction over an RxJava {@link CompletableObserver} that allows associating
 * a resource with it.
 * <p>
 * All methods are safe to call from multiple threads.
 * <p>
 * Calling onComplete or onError multiple times has no effect.
 */
public interface CompletableEmitter {

    /**
     * Signal the completion.
     */
    void onComplete();

    /**
     * Signal an exception.
     * @param t the exception, not null
     */
    void onError(Throwable t);

    /**
     * Sets a Disposable on this emitter; any previous Disposable
     * or Cancellation will be disposed/cancelled.
     * @param d the disposable, null is allowed
     */
    void setDisposable(Disposable d);

    /**
     * Sets a Cancellable on this emitter; any previous Disposable
     * or Cancellation will be disposed/cancelled.
     * @param c the cancellable resource, null is allowed
     */
    void setCancellable(Cancellable c);

    /**
     * Returns true if the downstream disposed the sequence.
     * @return true if the downstream disposed the sequence
     */
    boolean isDisposed();
}
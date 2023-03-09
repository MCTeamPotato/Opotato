package com.teampotato.opotato.util.nec;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class StateManager {

    // Use WeakReference to allow garbage collection, preventing memory leaks
    private static final Set<WeakReference<IResettable>> resettableRefs = new HashSet<>();

    public static void resetStates() {
        Iterator<WeakReference<IResettable>> iterator = resettableRefs.iterator();
        while (iterator.hasNext()) {
            IResettable ref = iterator.next().get();
            if (ref != null) {
                ref.resetState();
            } else {
                iterator.remove();
            }
        }
    }

    public interface IResettable {

        default void register() {
            resettableRefs.add(new WeakReference<>(this));
        }

        void resetState();
    }
}


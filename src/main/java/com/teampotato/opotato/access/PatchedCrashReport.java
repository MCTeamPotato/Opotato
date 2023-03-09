package com.teampotato.opotato.access;

import com.teampotato.opotato.util.nec.platform.CommonModMetadata;

import java.util.Set;

public interface PatchedCrashReport {
    Set<CommonModMetadata> getSuspectedMods();
    interface Element {
        String invokeGetName();
        String invokeGetDetail();
    }
}


package com.teampotato.opotato.patches;

import com.teampotato.opotato.platform.CommonModMetadata;

import java.util.Set;

public interface PatchedCrashReport {
    Set<CommonModMetadata> getSuspectedMods();
    interface Element {
        String invokeGetName();
        String invokeGetDetail();
    }
}


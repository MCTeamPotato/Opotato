package com.teampotato.opotato.config.mods.stxck;

import java.util.List;
import java.util.Set;

public interface StxckCommonConfig {
    double getMaxMergeDistanceHorizontal();
    double getMaxMergeDistanceVertical();
    int getMaxSize();
    boolean isEnableForUnstackableItem();
    List<? extends String> getItemBlackList();
}

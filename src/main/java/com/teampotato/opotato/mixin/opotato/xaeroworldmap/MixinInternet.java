package com.teampotato.opotato.mixin.opotato.xaeroworldmap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import xaero.map.WorldMap;
import xaero.map.misc.Internet;

@Mixin(value = Internet.class, remap = false)
public abstract class MixinInternet {
    /**
     * @author Kasualix
     * @reason Remove Internet connection behavior
     */
    @Overwrite
    public static void checkModVersion() {
        WorldMap.isOutdated = false;
    }
}

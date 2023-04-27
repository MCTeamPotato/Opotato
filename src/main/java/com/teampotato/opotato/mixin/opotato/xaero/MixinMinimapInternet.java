package com.teampotato.opotato.mixin.opotato.xaero;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import xaero.map.WorldMap;
import xaero.map.misc.Internet;

@Mixin(Internet.class)
public abstract class MixinMinimapInternet {
    /**
     * @author Kasualix
     * @reason Remove Internet connection behavior
     */
    @Overwrite
    public static void checkModVersion() {
        WorldMap.isOutdated = false;
    }
}

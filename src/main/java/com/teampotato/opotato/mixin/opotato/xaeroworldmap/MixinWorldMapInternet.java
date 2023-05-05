package com.teampotato.opotato.mixin.opotato.xaeroworldmap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import xaero.common.IXaeroMinimap;
import xaero.common.misc.Internet;

@Mixin(value = Internet.class, remap = false)
public abstract class MixinWorldMapInternet {
    /**
     * @author Kasualix
     * @reason Remove Internet connection behavior
     */
    @Overwrite
    public static void checkModVersion(IXaeroMinimap modMain) {
        modMain.setOutdated(false);
    }
}

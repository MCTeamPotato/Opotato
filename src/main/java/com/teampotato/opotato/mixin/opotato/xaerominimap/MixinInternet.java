package com.teampotato.opotato.mixin.opotato.xaerominimap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import xaero.common.IXaeroMinimap;
import xaero.common.misc.Internet;

@Mixin(Internet.class)
public abstract class MixinInternet {
    /**
     * @author Kasualix
     * @reason Remove Internet connection behavior
     */
    @Overwrite(remap = false)
    public static void checkModVersion(IXaeroMinimap modMain) {
        modMain.setOutdated(false);
    }
}

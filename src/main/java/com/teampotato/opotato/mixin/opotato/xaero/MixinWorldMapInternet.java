package com.teampotato.opotato.mixin.opotato.xaero;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import xaero.common.IXaeroMinimap;
import xaero.common.misc.Internet;

@Mixin(Internet.class)
public class MixinWorldMapInternet {
    /**
     * @author Kasualix
     * @reason Remove Internet connection behavior
     */
    @Overwrite
    public static void checkModVersion(IXaeroMinimap modMain) {
        modMain.setOutdated(false);
    }
}

package com.teampotato.opotato.mixin.opotato.xaerominimap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import xaero.map.patreon.Patreon;

import java.util.HashMap;

@Mixin(value = Patreon.class, remap = false)
public abstract class MixinPatreon {
    @Shadow private static boolean shouldRedirectToMinimap;

    @Shadow private static HashMap<String, Object> mods;

    @Shadow private static boolean loaded;

    @Shadow public static void loadSettings() {}

    /**
     * @author Kasualix
     * @reason Remove Internet connection behavior
     */
    @SuppressWarnings("SynchronizeOnNonFinalField")
    @Overwrite
    public static void checkPatreon() {
        if (shouldRedirectToMinimap) {
            xaero.common.patreon.Patreon.checkPatreon();
        } else {
            synchronized(mods) {
                if (!loaded) {
                    loadSettings();
                    mods.clear();
                    loaded = true;
                }
            }
        }
    }
}

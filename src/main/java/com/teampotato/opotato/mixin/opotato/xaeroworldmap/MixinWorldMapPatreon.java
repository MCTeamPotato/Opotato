package com.teampotato.opotato.mixin.opotato.xaeroworldmap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import xaero.common.patreon.Patreon;

import java.util.HashMap;

@Mixin(value = Patreon.class, remap = false)
public abstract class MixinWorldMapPatreon {
    @Shadow private static boolean loaded;
    @Shadow private static HashMap<String, Object> mods;
    @Shadow public static void loadSettings() {}

    /**
     * @author Kasualix
     * @reason Remove Internet connection behavior
     */
    @Overwrite
    public static void checkPatreon() {
        synchronized(mods) {
            if (!loaded) {
                loadSettings();
                mods.clear();
                loaded = true;
            }
        }
    }
}

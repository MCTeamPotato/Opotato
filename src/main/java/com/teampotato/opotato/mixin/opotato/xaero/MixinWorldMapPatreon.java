package com.teampotato.opotato.mixin.opotato.xaero;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import xaero.common.patreon.Patreon;
import java.util.ArrayList;
import java.util.HashMap;

@Mixin(value = Patreon.class, remap = false)
public abstract class MixinWorldMapPatreon {
    @Shadow private static HashMap<Integer, ArrayList<String>> patrons;
    @Shadow private static boolean loaded;
    @Shadow private static HashMap<String, Object> mods;
    @Shadow public static void loadSettings() {}
    @Shadow public static HashMap<String, Object> getMods() {
        return mods;
    }

    /**
     * @author Kasualix
     * @reason Remove Internet connection behavior
     */
    @Overwrite
    public static void checkPatreon() {
        synchronized(patrons) {
            if (!loaded) {
                loadSettings();
                patrons.clear();
                getMods().clear();
            }
        }
    }
}

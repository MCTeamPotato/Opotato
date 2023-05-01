package com.teampotato.opotato.mixin.opotato.xaero;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import xaero.map.patreon.Patreon;
import java.util.ArrayList;
import java.util.HashMap;

@Mixin(value = Patreon.class, remap = false)
public abstract class MixinMinimapPatreon {
    @Shadow private static boolean shouldRedirectToMinimap;
    @Shadow public static void loadSettings() {}
    @Shadow private static boolean loaded;
    @Shadow private static HashMap<Integer, ArrayList<String>> patrons;
    @Shadow private static HashMap<String, Object> mods;

    /**
     * @author Kasualix
     * @reason Remove Internet connection behavior
     */
    @Overwrite
    public static void checkPatreon() {
        if (shouldRedirectToMinimap) {
            xaero.common.patreon.Patreon.checkPatreon();
        } else {
            synchronized(patrons) {
                if (!loaded) {
                    loadSettings();
                    patrons.clear();
                    mods.clear();
                }
            }
        }
    }
}

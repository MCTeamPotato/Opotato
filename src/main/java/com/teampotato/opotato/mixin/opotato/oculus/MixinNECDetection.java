package com.teampotato.opotato.mixin.opotato.oculus;

import net.coderbot.iris.Iris;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = Iris.class, remap = false)
public abstract class MixinNECDetection {
    /**
     * @author Kasualix
     * @reason Remove NEC check
     */
    @Overwrite
    public static boolean hasNotEnoughCrashes() {
        return false;
    }
}
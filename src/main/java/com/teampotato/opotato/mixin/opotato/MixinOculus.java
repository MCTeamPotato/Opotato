package com.teampotato.opotato.mixin.opotato;

import net.coderbot.iris.Iris;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Iris.class)
public class MixinOculus {
    /**
     * @author Doctor Who
     * @reason Saving The World
     */
    @Overwrite
    public static boolean hasNotEnoughCrashes() {
        return false;
    }
}

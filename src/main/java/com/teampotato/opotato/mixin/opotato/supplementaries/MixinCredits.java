package com.teampotato.opotato.mixin.opotato.supplementaries;

import net.mehvahdjukaar.supplementaries.common.Credits;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = Credits.class, remap = false)
public abstract class MixinCredits {
    /**
     * @author Kasualix
     * @reason Remove Internet connection behavior
     */
    @Overwrite
    public static void fetchFromServer() {}
}

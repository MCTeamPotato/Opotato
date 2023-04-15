package com.teampotato.opotato.mixin.opotato.placebo;

import com.teampotato.opotato.Opotato;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import shadows.placebo.patreon.WingsManager;

@Mixin(value = WingsManager.class, remap = false)
public class MixinWingsManager {
    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;start()V"))
    private static void onThreadStart(Thread instance) {
        Opotato.LOGGER.info("[Opotato] No placebo loading patreon wings data!");
    }
}

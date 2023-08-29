package com.teampotato.opotato.mixin.opotato.placebo;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import shadows.placebo.patreon.WingsManager;

@Mixin(value = WingsManager.class, remap = false)
public abstract class MixinWingsManager {
    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;start()V"))
    private static void onThreadStart(Thread instance) {}
}

package com.teampotato.opotato.mixin.opotato.blueskies;

import com.legacy.blue_skies.MLSupporter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MLSupporter.GetSupportersThread.class)
public class MixinMLSupporters {
    @Inject(method = "run", at = @At("HEAD"), cancellable = true)
    private static void onRun(CallbackInfo ci) {
        ci.cancel();
    }
}

package com.teampotato.opotato.mixin.opotato.blueskies;

import com.legacy.blue_skies.MLSupporter;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MLSupporter.GetSupportersThread.class, remap = false)
public class MixinMLSupporters {
    @Inject(method = "run", at = @At("HEAD"), cancellable = true)
    private void onRun(@NotNull CallbackInfo ci) {
        ci.cancel();
    }
}

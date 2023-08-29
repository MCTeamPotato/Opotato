package com.teampotato.opotato.mixin.opotato.supplementaries;

import net.mehvahdjukaar.supplementaries.common.Credits;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Credits.class, remap = false)
public abstract class MixinCredits {
    @Inject(method = "fetchFromServer", at = @At("HEAD"), cancellable = true)
    private static void onConnectInternet(CallbackInfo ci) {
        ci.cancel();
    }
}

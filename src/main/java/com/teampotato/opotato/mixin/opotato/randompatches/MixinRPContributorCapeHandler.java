package com.teampotato.opotato.mixin.opotato.randompatches;

import com.therandomlabs.randompatches.client.RPContributorCapeHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RPContributorCapeHandler.class, remap = false)
public abstract class MixinRPContributorCapeHandler {
    @Shadow private static boolean attemptingDownload;

    @Inject(method = "downloadContributorList", at = @At("HEAD"), cancellable = true)
    private static void onConnect(CallbackInfo ci) {
        attemptingDownload = true;
        ci.cancel();
    }
}
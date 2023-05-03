package com.teampotato.opotato.mixin.opotato.quark;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.base.handler.ContributorRewardHandler;

@Mixin(value = ContributorRewardHandler.class, remap = false)
public class MixinContributorRewardHandler {
    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private static void onInit(CallbackInfo ci) {
        ci.cancel();
    }
}

package com.teampotato.opotato.mixin.opotato.quark;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.base.handler.ContributorRewardHandler;

@Mixin(value = ContributorRewardHandler.class, remap = false)
public abstract class MixinContributorRewardHandler {
    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private static void onInit(@NotNull CallbackInfo ci) {
        ci.cancel();
    }
}

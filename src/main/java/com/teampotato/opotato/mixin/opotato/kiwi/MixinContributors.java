package com.teampotato.opotato.mixin.opotato.kiwi;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snownee.kiwi.contributor.Contributors;
import snownee.kiwi.contributor.ITierProvider;

@Mixin(value = Contributors.class, remap = false)
public class MixinContributors {
    @Inject(method = "registerTierProvider", at = @At("HEAD"), cancellable = true)
    private static void disableInit(ITierProvider rewardProvider, CallbackInfo ci) {
        ci.cancel();
    }
}

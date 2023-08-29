package com.teampotato.opotato.mixin.opotato.elenaidodge;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.elenai.elenaidodge2.util.PatronRewardHandler$ThreadContributorListLoader", remap = false)
public abstract class MixinPatronRewardHandler {
    @Inject(method = "run", at = @At("HEAD"), cancellable = true)
    private void onConnect(CallbackInfo ci) {
        ci.cancel();
    }
}
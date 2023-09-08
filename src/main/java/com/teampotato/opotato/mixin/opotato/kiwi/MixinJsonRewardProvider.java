package com.teampotato.opotato.mixin.opotato.kiwi;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import snownee.kiwi.contributor.impl.JsonRewardProvider;

@Mixin(value = JsonRewardProvider.class, remap = false)
public class MixinJsonRewardProvider {
    @Inject(method = "load", at = @At("HEAD"), cancellable = true)
    private void onLoad(String url, @NotNull CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
        cir.cancel();
    }
}

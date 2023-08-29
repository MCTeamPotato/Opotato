package com.teampotato.opotato.mixin.opotato.modernui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(targets = "icyllis.modernui.forge.Registration", remap = false)
public abstract class MixinRegistration {
    @SuppressWarnings({"rawtypes", "OptionalUsedAsFieldOrParameterType"})
    @Redirect(method = "setupCommon", at = @At(value = "INVOKE", target = "Ljava/util/Optional;isPresent()Z", remap = false))
    private static boolean whyOKWhenKiwi(Optional instance) {
        return false;
    }
}

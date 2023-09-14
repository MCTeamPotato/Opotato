package com.teampotato.opotato.mixin.opotato.ldlib;

import com.lowdragmc.lowdraglib.jei.JEIClientEventHandler;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = JEIClientEventHandler.class, remap = false)
public abstract class MixinJEIClientEventHandler {
    @Inject(method = "onRecipesUpdatedEventEvent", at = @At("HEAD"), cancellable = true)
    private static void onEvent(@NotNull CallbackInfo ci) {
        ci.cancel();
    }
}
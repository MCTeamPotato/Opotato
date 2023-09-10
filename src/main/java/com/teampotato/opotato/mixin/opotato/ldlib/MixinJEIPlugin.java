package com.teampotato.opotato.mixin.opotato.ldlib;

import com.lowdragmc.lowdraglib.jei.JEIPlugin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = JEIPlugin.class, remap = false)
public abstract class MixinJEIPlugin {
    @Inject(method = "setupInputHandler", at = @At("HEAD"), cancellable = true)
    private static void onSetup(CallbackInfo ci) {
        ci.cancel();
    }
}
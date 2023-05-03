package com.teampotato.opotato.mixin.opotato.ldlib;

import com.lowdragmc.lowdraglib.jei.JEIPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = JEIPlugin.class, remap = false)
public abstract class MixinJEIPlugin {
    @Shadow public static void setupInputHandler() {}

    @Inject(method = "onRuntimeAvailable", at = @At("HEAD"))
    private void setupInputHandlerOnRuntimeAvailable(IJeiRuntime jeiRuntime, CallbackInfo ci) {
        setupInputHandler();
    }
}

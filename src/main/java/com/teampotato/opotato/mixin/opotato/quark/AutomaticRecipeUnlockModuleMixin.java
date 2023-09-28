package com.teampotato.opotato.mixin.opotato.quark;

import com.teampotato.opotato.mixin.EarlySetupInitializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.content.tweaks.module.AutomaticRecipeUnlockModule;

@Mixin(value = AutomaticRecipeUnlockModule.class, remap = false)
public abstract class AutomaticRecipeUnlockModuleMixin {
    @Inject(method = "onPlayerLoggedIn", at = @At("HEAD"), cancellable = true)
    private void onPlayerLogIn(CallbackInfo ci) {
        if (EarlySetupInitializer.isNotEnoughRecipeBookLoaded) ci.cancel();
    }

    @Mixin(value = AutomaticRecipeUnlockModule.class, remap = false)
    public abstract static class Client {
        @Inject(method = {"onInitGui", "clientTick"}, at = @At("HEAD"), cancellable = true)
        private void onPlayerLogIn(CallbackInfo ci) {
            if (EarlySetupInitializer.isNotEnoughRecipeBookLoaded) ci.cancel();
        }
    }
}

package com.teampotato.opotato.mixin.opotato.blueskies;

import com.legacy.blue_skies.BlueSkiesConfig;
import com.teampotato.opotato.config.mods.BlueSkiesExtraConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlueSkiesConfig.CommonConfig.class, remap = false)
public class MixinBlueSkiesCommonConfig {
    @Inject(method = "isModAllowedForFeatureGen", at = @At("HEAD"), cancellable = true)
    private void allowEveryModFeatureGen(String mod, CallbackInfoReturnable<Boolean> cir) {
        if (BlueSkiesExtraConfig.allowEveryModFeatureGenInTheDims.get()) cir.setReturnValue(true);
    }
}

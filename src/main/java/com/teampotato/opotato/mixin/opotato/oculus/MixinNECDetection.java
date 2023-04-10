package com.teampotato.opotato.mixin.opotato.oculus;

import net.coderbot.iris.Iris;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Iris.class, remap = false)
public class MixinNECDetection {
    @Inject(method = "hasNotEnoughCrashes", at = @At("HEAD"), cancellable = true)
    private static void noNECWarn(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
        cir.cancel();
    }
}
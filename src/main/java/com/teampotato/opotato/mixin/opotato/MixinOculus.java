package com.teampotato.opotato.mixin.opotato;

import net.coderbot.iris.Iris;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Iris.class, remap = false)
public class MixinOculus {
    @Inject(method = "hasNotEnoughCrashes", at = @At(value = "HEAD"), cancellable = true)
    public static boolean removeNEC(CallbackInfo info) {
        info.cancel();
        return false;
    }
}
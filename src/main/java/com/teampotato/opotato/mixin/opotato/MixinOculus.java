package com.teampotato.opotato.mixin.opotato;

import net.coderbot.iris.Iris;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Iris.class)
public class MixinOculus {
    @Inject(at = {@At(value = "HEAD", remap = false)}, method = "hasNotEnoughCrashes", cancellable = true)
    public static boolean hasNotEnoughCrashes(CallbackInfo info) {
        info.cancel();
        return false;
    }
}

package com.teampotato.opotato.mixin.opotato.citadel;

import com.github.alexthe666.citadel.web.WebHelper;
import com.teampotato.opotato.Opotato;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.BufferedReader;

@Mixin(value = WebHelper.class, remap = false)
public class MixinWebHelper {
    @Inject(method = "getURLContents", at = @At("HEAD"), cancellable = true)
    private static void on_getURLContents(String urlString, String backupFileLoc, CallbackInfoReturnable<BufferedReader> cir) {
        cir.setReturnValue(null);
        cir.cancel();
    }
}

package com.teampotato.opotato.mixin.opotato.forge;

import net.minecraftforge.fml.loading.FMLConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FMLConfig.class, remap = false)
public class MixinFMLConfig {
    @Inject(method = "runVersionCheck", at = @At("HEAD"), cancellable = true)
    private static void noVersionCheck(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
        cir.cancel();
    }
}

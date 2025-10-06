package com.teampotato.opotato.mixin.opotato.minecraft;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Inject(method = "isFlightAllowed", at = @At("HEAD"), cancellable = true)
    private void onFly(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}

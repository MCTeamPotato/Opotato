package com.teampotato.opotato.mixin.smoothmenu;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("SpellCheckingInspection")
@Mixin(Minecraft.class)
public abstract class SmoothMenuMixin {
    @Inject(method = "getFramerateLimit", at = @At("HEAD"), cancellable = true)
    private void onGetFramerateLimit(CallbackInfoReturnable<Integer> ci) {
        ci.setReturnValue(((Minecraft)(Object)this).getWindow().getFramerateLimit());
        ci.cancel();
    }
}

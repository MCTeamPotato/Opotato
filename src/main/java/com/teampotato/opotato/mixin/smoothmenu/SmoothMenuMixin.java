package com.teampotato.opotato.mixin.smoothmenu;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class SmoothMenuMixin {
    @Inject(at=@At("HEAD"), method = "getFramerateLimit()I", cancellable = true)
    private void getFramerateLimit(CallbackInfoReturnable<Integer> ci) {
        ci.setReturnValue(((Minecraft)(Object)this).getWindow().getFramerateLimit());
    }
}

package com.teampotato.opotato.mixin.nohurtcam;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teampotato.opotato.event.ClientEvents;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(at = @At("HEAD"), method = "bobHurt", cancellable = true)
    public void bobViewWhenHurt(MatrixStack matrixStack_1, float float_1, CallbackInfo ci) {
        if (ClientEvents.toggledOn) ci.cancel();
    }
}

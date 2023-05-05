package com.teampotato.opotato.mixin.opotato.gender;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wildfire.render.GenderLayer;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GenderLayer.class, remap = false)
public abstract class MixinGenderLayer {
    @Inject(method = "render(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/client/entity/player/AbstractClientPlayerEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    private void noRenderOnCorpse(MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity ent, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        ResourceLocation regName = ent.getType().getRegistryName();
        if (regName == null || !regName.toString().equals("minecraft:player")) ci.cancel();
    }
}

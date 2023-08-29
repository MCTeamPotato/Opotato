package com.teampotato.opotato.mixin.opotato.gender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wildfire.render.GenderLayer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GenderLayer.class)
public abstract class MixinGenderLayer {
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    private void noRenderOnCorpse(PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, AbstractClientPlayer ent, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        ResourceLocation regName = ent.getType().getRegistryName();
        if (regName == null || !regName.toString().equals("minecraft:player")) ci.cancel();
    }
}

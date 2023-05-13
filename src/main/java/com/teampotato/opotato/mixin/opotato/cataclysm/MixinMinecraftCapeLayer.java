package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.init.ModItems;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.inventory.EquipmentSlotType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CapeLayer.class, priority = 9999)
public abstract class MixinMinecraftCapeLayer {
    @Inject(method = "render(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/client/entity/player/AbstractClientPlayerEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    private void onRender(MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, AbstractClientPlayerEntity pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, CallbackInfo ci) {
        if (pLivingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem().equals(ModItems.IGNITIUM_ELYTRA_CHESTPLATE.get())) ci.cancel();
    }
}

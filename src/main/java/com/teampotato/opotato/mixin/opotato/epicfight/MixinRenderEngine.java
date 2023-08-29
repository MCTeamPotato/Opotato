package com.teampotato.opotato.mixin.opotato.epicfight;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teampotato.opotato.Opotato;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.client.events.engine.RenderEngine;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@Mixin(value = RenderEngine.class, remap = false)
public class MixinRenderEngine {
    @Inject(method = "renderEntityArmatureModel", at = @At("HEAD"))
    private void onRender(LivingEntity livingEntity, LivingEntityPatch<?> entitypatch, LivingEntityRenderer<? extends Entity, ?> renderer, MultiBufferSource buffer, PoseStack matStack, int packedLightIn, float partialTicks, CallbackInfo ci) {
        Opotato.LOGGER.error(livingEntity.getType().getRegistryName());
    }
}

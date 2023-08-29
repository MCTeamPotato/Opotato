package com.teampotato.opotato.mixin.opotato.epicfight;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.client.renderer.EpicFightRenderTypes;

@Mixin(value = EpicFightRenderTypes.class, remap = false)
public abstract class MixinEpicFightRenderTypes {
    @Shadow public static RenderType enchantedAnimatedArmor() {
        return ANIMATED_ARMOR_GLINT;
    }
    @Shadow @Final private static RenderType ANIMATED_ARMOR_GLINT;

    @Inject(method = "getArmorVertexBuilder", at = @At("HEAD"), cancellable = true)
    private static void onGetArmorVertexBuilder(MultiBufferSource buffer, RenderType renderType, boolean withGlint, CallbackInfoReturnable<VertexConsumer> cir) {
        if (buffer.getBuffer(enchantedAnimatedArmor()) != buffer.getBuffer(renderType)) return;
        cir.setReturnValue(buffer.getBuffer(renderType));
        cir.cancel();
    }
}
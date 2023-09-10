package com.teampotato.opotato.mixin.stxck.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teampotato.opotato.client.ItemCountRenderer;
import com.teampotato.opotato.util.stxck.Staaaaaaaaaaaack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Inject(
            method = "renderEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;render(Lnet/minecraft/world/entity/Entity;DDDFFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
            )
    )
    private void renderItemCount(
            Entity entity,
            double x, double y, double z,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            CallbackInfo ci
    ) {
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        if (entityRenderDispatcher.camera == null) return;
        EntityRenderDispatcherAccessor accessor = (EntityRenderDispatcherAccessor) entityRenderDispatcher;
        int maxDistance = Staaaaaaaaaaaack.clientConfig.getMinItemCountRenderDistance();
        if (entityRenderDispatcher.distanceToSqr(entity) > maxDistance * maxDistance) return;
        if (entity instanceof ItemEntity) {
            ItemEntity itemEntity = (ItemEntity) entity;
            Vec3 offset = entityRenderDispatcher.getRenderer(entity).getRenderOffset(entity, partialTicks);
            int light = entityRenderDispatcher.getPackedLightCoords(entity, partialTicks);
            double nx = Mth.lerp(partialTicks, entity.xOld, entity.getX()) - x + offset.x();
            double ny = Mth.lerp(partialTicks, entity.yOld, entity.getY()) - y + offset.y();
            double nz = Mth.lerp(partialTicks, entity.zOld, entity.getZ()) - z + offset.z();

            poseStack.pushPose();
            poseStack.translate(nx, ny, nz);
            ItemCountRenderer.renderItemCount(itemEntity, poseStack, bufferSource, light, accessor);
            poseStack.popPose();
        }
    }

}

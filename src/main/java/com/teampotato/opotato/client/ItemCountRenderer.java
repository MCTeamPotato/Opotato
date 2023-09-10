package com.teampotato.opotato.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.teampotato.opotato.util.stxck.Staaaaaaaaaaaack;
import com.teampotato.opotato.mixin.stxck.client.EntityRenderDispatcherAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;

import static com.teampotato.opotato.util.stxck.StxckUtil.getTotalCountForDisplay;

public class ItemCountRenderer {

    public static void renderItemCount(
            ItemEntity entity,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int light,
            EntityRenderDispatcherAccessor entityRenderDispatcher
    ) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && player.isShiftKeyDown()) return;

        getTotalCountForDisplay(entity).ifPresent(itemCount -> {
            float scale = 0.025f * (float) Staaaaaaaaaaaack.clientConfig.getOverlaySizeMultiplier();
            poseStack.pushPose();
            poseStack.translate(0d, entity.getBbHeight() + 0.75f, 0d);
            poseStack.mulPose(entityRenderDispatcher.getCameraOrientation());
            poseStack.scale(-scale, -scale, scale);

            Component component = Component.nullToEmpty(itemCount);
            Matrix4f matrix4f = poseStack.last().pose();
            float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            Font font = entityRenderDispatcher.getFont();
            int j = (int)(f1 * 255f) << 24;
            float f2 = (float)(-font.width(component) / 2);
            font.drawInBatch(component, f2, 0, 553648127, false, matrix4f, bufferSource, false, j, light);
            font.drawInBatch(component, f2, 0, -1, false, matrix4f, bufferSource, false, 0, light);

            poseStack.popPose();
        });
    }

}

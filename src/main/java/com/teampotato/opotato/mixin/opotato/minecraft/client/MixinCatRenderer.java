package com.teampotato.opotato.mixin.opotato.minecraft.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.teampotato.opotato.api.mutable.IAABB;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CatRenderer.class)
public abstract class MixinCatRenderer extends MobRenderer<Cat, CatModel<Cat>> {
    public MixinCatRenderer(EntityRenderDispatcher arg, CatModel<Cat> arg2, float f) {
        super(arg, arg2, f);
    }

    /**
     * @author Kasualix
     * @reason The {@link net.minecraft.world.level.Level#getEntitiesOfClass(Class, AABB)} method is very slow, tbh.
     */
    @Overwrite
    protected void setupRotations(Cat pEntityLiving, PoseStack pMatrixStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
        super.setupRotations(pEntityLiving, pMatrixStack, pAgeInTicks, pRotationYaw, pPartialTicks);
        float lieDownAmount = pEntityLiving.getLieDownAmount(pPartialTicks);
        if (lieDownAmount > 0.0F) {
            pMatrixStack.translate(0.4F * lieDownAmount, 0.15F * lieDownAmount, 0.1F * lieDownAmount);
            pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.rotLerp(lieDownAmount, 0.0F, 90.0F)));
            AABB box = ((IAABB)new AABB(pEntityLiving.blockPosition()))._inflate(2.0D, 2.0D, 2.0D);
            for(Player player : pEntityLiving.level.players()) {
                if (player.isSleeping() && box.contains(player.position())) {
                    pMatrixStack.translate(0.15F * lieDownAmount, 0.0D, 0.0D);
                    break;
                }
            }
        }
    }
}

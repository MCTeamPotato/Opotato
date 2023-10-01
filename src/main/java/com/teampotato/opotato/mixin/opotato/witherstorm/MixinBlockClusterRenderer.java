package com.teampotato.opotato.mixin.opotato.witherstorm;

import com.teampotato.opotato.config.mods.WitherStormExtraConfig;
import net.minecraft.client.renderer.culling.Frustum;
import nonamecrackers2.witherstormmod.client.renderer.entity.BlockClusterRenderer;
import nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.ThreadLocalRandom;

@Mixin(BlockClusterRenderer.class)
public abstract class MixinBlockClusterRenderer {
    @Unique
    private static final ThreadLocalRandom opotato$random = ThreadLocalRandom.current();
    @Inject(method = "shouldRender(Lnonamecrackers2/witherstormmod/common/entity/BlockClusterEntity;Lnet/minecraft/client/renderer/culling/Frustum;DDD)Z", at = @At("HEAD"), cancellable = true)
    private void reduceRendering(BlockClusterEntity entity, Frustum frustum, double p_225626_3_, double p_225626_5_, double p_225626_7_, CallbackInfoReturnable<Boolean> cir) {
        if (WitherStormExtraConfig.shouldRandomlyReduceBlockClusterRendering.get() && opotato$random.nextInt(4) <= 2) {
            cir.setReturnValue(false);
        }
    }
}

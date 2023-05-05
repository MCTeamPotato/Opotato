package com.teampotato.opotato.mixin.opotato.witherstormmod;

import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.client.renderer.culling.ClippingHelper;
import nonamecrackers2.witherstormmod.client.renderer.entity.BlockClusterRenderer;
import nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockClusterRenderer.class, remap = false)
public abstract class MixinBlockClusterRenderer {
    @Inject(method = "shouldRender(Lnonamecrackers2/witherstormmod/common/entity/BlockClusterEntity;Lnet/minecraft/client/renderer/culling/ClippingHelper;DDD)Z", at = @At("HEAD"), cancellable = true)
    private void extraShouldRender(BlockClusterEntity entity, ClippingHelper p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_, CallbackInfoReturnable<Boolean> cir) {
        if (PotatoCommonConfig.BLOCK_CLUSTER_RENDER_OPTIMIZATION.get() && (entity.level.random.nextInt(4) < 2 || entity.level.players().stream().noneMatch(player -> player.canSee(entity)))) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}

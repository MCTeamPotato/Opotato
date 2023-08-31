package com.teampotato.opotato.mixin.opotato.cataclysm.rubidium;

import com.teampotato.opotato.api.LightestEntity;
import me.jellysquid.mods.sodium.client.model.light.EntityLighter;
import me.jellysquid.mods.sodium.client.render.entity.EntityLightSampler;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityLighter.class, remap = false)
public abstract class MixinEntityLighter {
    @Inject(method = "getBlendedLight", at = @At("HEAD"), cancellable = true)
    private static <T extends Entity> void onGetLight(EntityLightSampler<T> lighter, T entity, float tickDelta, CallbackInfoReturnable<Integer> cir) {
        if (entity instanceof LightestEntity) {
            cir.setReturnValue(LightestEntity.MAX_LIGHT);
            cir.cancel();
        }
    }
}

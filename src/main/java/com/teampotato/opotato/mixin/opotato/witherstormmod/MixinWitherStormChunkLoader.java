package com.teampotato.opotato.mixin.opotato.witherstormmod;

import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.entity.Entity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import nonamecrackers2.witherstormmod.common.event.WitherStormChunkLoader;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import org.apache.commons.lang3.RandomUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherStormChunkLoader.class)
public class MixinWitherStormChunkLoader {
    @Inject(method = "onWorldTick", at = @At("HEAD"), cancellable = true)
    private static void onWitherStormChunkLoad(TickEvent.WorldTickEvent event, CallbackInfo ci) {
        if (
                !(event.world instanceof ServerWorld) ||
                (PotatoCommonConfig.REDUCE_THE_WITHER_STORM_CHUNK_ACTIVITY.get() && RandomUtils.nextInt(0, 2) == 1) ||
                event.world.players().isEmpty() ||
                ((ServerWorld) event.world).getEntities(WitherStormModEntityTypes.WITHER_STORM, Entity::isAlive).isEmpty()
        ) ci.cancel();
    }
}

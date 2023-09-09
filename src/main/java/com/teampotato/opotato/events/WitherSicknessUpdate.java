package com.teampotato.opotato.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.common.capability.WitherStormBowelsManager;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class WitherSicknessUpdate {
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.@NotNull LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();
        Level level = entity.level;
        if (level instanceof ServerLevel) {
            boolean isBowels = level.dimension().location().equals(WitherStormMod.bowelsLocation());
            if (entity.getY() < 50.0 && isBowels) WitherStormBowelsManager.leave((ServerLevel) level, entity, null);
            ServerLevel serverLevel = (ServerLevel) level;
            entity.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER).ifPresent(tracker -> {
                if (!tracker.isActuallyImmune()) {
                    boolean nearby = isBowels;
                    if (!nearby) {
                        for (UUID uuid : EntitiesCacheEvent.witherStorms.keySet()) {
                            WitherStormEntity witherStormEntity = (WitherStormEntity) serverLevel.getEntity(uuid);
                            if (witherStormEntity != null && witherStormEntity.getPhase() > 1) {
                                nearby = witherStormEntity.isEntityNearby(entity);
                                if (nearby) break;
                            }
                        }
                    }
                    tracker.setNearStorm(nearby);
                }
                tracker.tick();
            });
        }
    }
}

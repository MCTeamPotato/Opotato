package com.teampotato.opotato.mixin.opotato.witherstormmod;

import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.event.WitherSicknessEvents;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = WitherSicknessEvents.class, remap = false)
public abstract class MixinWitherSicknessEvents {
    /**
     * @author Kasualix
     * @reason Optimize event
     */
    @Overwrite
    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.world instanceof ServerWorld)) return;
        ServerWorld world = (ServerWorld) event.world;
        for (Entity entity : world.getEntities(null, entity -> {
            boolean capPresent = entity.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER).isPresent();
            boolean isLiving = entity instanceof LivingEntity;
            if (PotatoCommonConfig.LET_WITHER_SICKNESS_ONLY_TAKE_EFFECT_ON_PLAYERS.get()) return (entity instanceof PlayerEntity && capPresent);
            if (PotatoCommonConfig.LET_MOBS_IMMUNE_TO_WITHER_SICKNESS_TICKING_SYSTEM.get()) return (isLiving && !(entity instanceof MonsterEntity) && capPresent);
            if (PotatoCommonConfig.LET_ANIMALS_IMMUNE_TO_WITHER_SICKNESS_TICKING_SYSTEM.get()) return (isLiving && !(entity instanceof AnimalEntity) && capPresent);
            return entity instanceof LivingEntity;
        })) {
            entity.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER).ifPresent(tracker -> {
                if (!tracker.isActuallyImmune()) {
                    boolean nearby = entity.level.dimension().location().equals(WitherStormMod.bowelsLocation());
                    WitherStormEntity witherStormEntity = getWitherStorm(world);
                    if (witherStormEntity != null) nearby = witherStormEntity.isEntityNearby(entity);
                    tracker.setNearStorm(nearby);
                }
                tracker.tick();
            });
        }
    }

    private static @Nullable WitherStormEntity getWitherStorm(ServerWorld world) {
        for (Entity entity : world.getAllEntities()) {
            if (entity instanceof WitherStormEntity && ((WitherStormEntity)entity).getPhase() > 1) return (WitherStormEntity) entity;
        }
        return null;
    }
}
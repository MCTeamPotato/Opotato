package com.teampotato.opotato.mixin.opotato.witherstormmod;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.event.WitherSicknessEvents;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static com.teampotato.opotato.config.PotatoCommonConfig.LET_ANIMALS_IMMUNE_TO_WITHER_SICKNESS_TICKING_SYSTEM;
import static com.teampotato.opotato.config.PotatoCommonConfig.LET_WITHER_SICKNESS_ONLY_TAKE_EFFECT_ON_PLAYERS;

@Mixin(value = WitherSicknessEvents.class, remap = false)
public class MixinWitherSicknessEvents {
    /**
     * @author Kasualix
     * @reason Optimize someone's shit
     */
    @Overwrite
    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.world.isClientSide || !WitherStormModConfig.SERVER.witherSicknessEnabled.get()) return;
        ServerWorld world = (ServerWorld) event.world;
        world
                .getEntities()
                .filter(entity -> entity instanceof LivingEntity && entity.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER).isPresent())
                .forEach(entity -> {
                    if ((LET_WITHER_SICKNESS_ONLY_TAKE_EFFECT_ON_PLAYERS.get() && !(entity instanceof PlayerEntity)) || (LET_ANIMALS_IMMUNE_TO_WITHER_SICKNESS_TICKING_SYSTEM.get() && entity instanceof MonsterEntity) || (LET_ANIMALS_IMMUNE_TO_WITHER_SICKNESS_TICKING_SYSTEM.get() && entity instanceof AnimalEntity)) return;
                    entity.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER).ifPresent((tracker) -> {
                        if (!tracker.isActuallyImmune()) {
                            boolean nearby = entity.level.dimension().location().equals(WitherStormMod.bowelsLocation());
                            if (!world.getEntities(WitherStormModEntityTypes.WITHER_STORM, storm -> storm instanceof WitherStormEntity && ((WitherStormEntity)storm).getPhase() > 1 && ((WitherStormEntity)storm).isEntityNearby(entity)).isEmpty()) nearby = true;
                            tracker.setNearStorm(nearby);
                        }
                        tracker.tick();
                    });
                });
    }
}

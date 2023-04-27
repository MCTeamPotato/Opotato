package com.teampotato.opotato.mixin.opotato.witherstormmod;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.event.WitherSicknessEvents;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.stream.StreamSupport;

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
        ServerLevel world = (ServerLevel) event.world;
        StreamSupport.stream(world
                .getEntities().getAll().spliterator(), false)
                .filter(entity -> entity instanceof LivingEntity && entity.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER).isPresent())
                .forEach(entity -> {
                    if ((LET_WITHER_SICKNESS_ONLY_TAKE_EFFECT_ON_PLAYERS.get() && !(entity instanceof Player)) || (LET_ANIMALS_IMMUNE_TO_WITHER_SICKNESS_TICKING_SYSTEM.get() && entity instanceof Monster) || (LET_ANIMALS_IMMUNE_TO_WITHER_SICKNESS_TICKING_SYSTEM.get() && entity instanceof Animal)) return;
                    entity.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER).ifPresent((tracker) -> {
                        if (!tracker.isActuallyImmune()) {
                            boolean nearby = entity.level.dimension().location().equals(WitherStormMod.bowelsLocation());
                            if (!world.getEntities(WitherStormModEntityTypes.WITHER_STORM.get(), storm -> storm != null && storm.getPhase() > 1 && storm.isEntityNearby(entity)).isEmpty()) nearby = true;
                            tracker.setNearStorm(nearby);
                        }
                        tracker.tick();
                    });
                });
    }
}

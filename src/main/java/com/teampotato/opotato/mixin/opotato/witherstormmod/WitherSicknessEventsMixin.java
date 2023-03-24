package com.teampotato.opotato.mixin.opotato.witherstormmod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.event.WitherSicknessEvents;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Mixin(value = WitherSicknessEvents.class, remap = false)
public class WitherSicknessEventsMixin {

    /**
     * @author Doctor Who
     * @reason Saving The World
     */
    @Overwrite
    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {

            CopyOnWriteArrayList<Entity> entities = StreamSupport.stream(((ServerWorld) event.world).getAllEntities().spliterator(), false)
                    .collect(Collectors.toCollection(CopyOnWriteArrayList::new));

            if (entities.stream().noneMatch(checker -> checker instanceof WitherStormEntity)) return;
            if (entities.stream().noneMatch(checker -> checker instanceof PlayerEntity)) return;

            Map<Class<? extends Entity>, List<Entity>> entityGroups = entities.stream().collect(Collectors.groupingBy(Entity::getClass));

            for (List<Entity> livingGroup : entityGroups.entrySet().stream().filter(entry -> LivingEntity.class.isAssignableFrom(entry.getKey())).map(Map.Entry::getValue).collect(Collectors.toList())) {
                Optional<List<Entity>> witherStormGroup = livingGroup.stream()
                        .flatMap(livingEntity -> entityGroups.entrySet().stream()
                                .filter(entry -> entry.getKey().equals(WitherStormEntity.class))
                                .map(Map.Entry::getValue)
                                .collect(Collectors.toList())
                                .stream()
                                .filter(group -> group.stream().anyMatch(entity -> ((WitherStormEntity) entity).isEntityNearby(livingEntity))))
                        .findFirst();

                boolean nearbyStorm = witherStormGroup.isPresent() || livingGroup.stream().anyMatch(entity -> entity.level.dimension().location().equals(WitherStormMod.bowelsLocation()));

                livingGroup.stream()
                        .filter(LivingEntity.class::isInstance)
                        .map(LivingEntity.class::cast)
                        .forEach(living -> living.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER)
                                .ifPresent(tracker -> {
                                    if (!tracker.isActuallyImmune()) tracker.setNearStorm(nearbyStorm);
                                    tracker.tick();
                                }));
            }
        }
    }
}

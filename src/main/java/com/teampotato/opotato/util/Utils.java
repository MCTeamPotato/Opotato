package com.teampotato.opotato.util;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class Utils {
    public static @NotNull List<Villager> getNearbyVillagersWithCondition(final @NotNull Villager villager, final Predicate<Villager> villagerPredicate) {
        final ObjectArrayList<Villager> nearbyVillagers = new ObjectArrayList<>();
        for (LivingEntity entity : villager.getBrain().getMemory(MemoryModuleType.LIVING_ENTITIES).orElse(Collections.emptyList())) {
            if (entity instanceof Villager && entity != villager) {
                final Villager nearbyVillager = (Villager) entity;
                if (nearbyVillager.isAlive() && villagerPredicate.test(nearbyVillager)) nearbyVillagers.add(nearbyVillager);
            }
        }
        return nearbyVillagers;
    }
}

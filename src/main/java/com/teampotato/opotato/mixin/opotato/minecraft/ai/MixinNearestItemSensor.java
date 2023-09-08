package com.teampotato.opotato.mixin.opotato.minecraft.ai;

import com.teampotato.opotato.events.EntitiesCacheEvent;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestItemSensor;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Comparator;
import java.util.UUID;

@Mixin(NearestItemSensor.class)
public abstract class MixinNearestItemSensor {
    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    protected void doTick(ServerLevel level, @NotNull Mob entity) {
        ObjectArrayList<ItemEntity> itemEntities = new ObjectArrayList<>();
        AABB box = entity.getBoundingBox().inflate(8.0, 4.0, 8.0);
        for (UUID itemEntityUUID : EntitiesCacheEvent.itemEntities) {
            ItemEntity itemEntity = (ItemEntity) level.getEntity(itemEntityUUID);
            if (itemEntity == null) continue;
            if (box.contains(itemEntity.position())) itemEntities.add(itemEntity);
        }

        // Sort the list based on distance
        itemEntities.sort(Comparator.comparingDouble(entity::distanceToSqr));

        ItemEntity nearestWantedItem = null;

        for (ItemEntity itemEntity : itemEntities) {
            if (entity.wantsToPickUp(itemEntity.getItem()) && itemEntity.closerThan(entity, 9.0) && entity.canSee(itemEntity)) {
                nearestWantedItem = itemEntity;
                break;
            }
        }

        entity.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, nearestWantedItem);
    }
}

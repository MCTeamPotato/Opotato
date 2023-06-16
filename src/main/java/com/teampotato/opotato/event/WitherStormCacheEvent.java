package com.teampotato.opotato.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

import java.util.UUID;

public class WitherStormCacheEvent {
    public static UUID initialUUID = new UUID(114514, 1919810);
    public static UUID witherStormUUID = initialUUID;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onWitherStormJoin(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof WitherStormEntity && !entity.level.isClientSide && !event.isCanceled()) {
            if (witherStormUUID.equals(initialUUID)) {
                witherStormUUID = entity.getUUID();
            } else {
                event.setCanceled(true);
                entity.remove();
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onWitherStormDie(LivingDeathEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (!event.isCanceled() && entity instanceof CommandBlockEntity && !entity.level.isClientSide) witherStormUUID = initialUUID;
    }
}

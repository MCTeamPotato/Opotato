package com.teampotato.opotato.events;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;

import java.util.UUID;

public class WitherStormCacheEvents {
    public static UUID witherStormUUID = null;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof CommandBlockEntity && !entity.level.isClientSide && !event.isCanceled()) witherStormUUID = null;
    }
}

package com.teampotato.opotato.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class DEUFix {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onEntityJoinWold(EntityJoinWorldEvent event) {
        if (event.getWorld() instanceof ServerLevel && !event.isCanceled()) {
            Entity entity = event.getEntity();
            if (entity instanceof ServerPlayer) return;
            ServerLevel serverLevel = (ServerLevel) event.getWorld();
            Entity existing = serverLevel.getEntity(entity.getUUID());
            if (existing == null || existing == entity) return;
            UUID newUUID = Mth.createInsecureUUID();
            while (serverLevel.getEntity(newUUID) != null) newUUID = Mth.createInsecureUUID();
            entity.setUUID(newUUID);
        }
    }
}

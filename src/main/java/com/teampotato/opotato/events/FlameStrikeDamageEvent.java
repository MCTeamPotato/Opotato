package com.teampotato.opotato.events;

import L_Ender.cataclysm.entity.effect.Flame_Strike_Entity;
import com.teampotato.opotato.api.IFlameStrikeEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class FlameStrikeDamageEvent {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingTick(LivingEvent.LivingUpdateEvent event) {
        if (event.isCanceled()) return;
        LivingEntity entity = event.getEntityLiving();
        Level level = entity.level;
        if (level instanceof ServerLevel && !EntitiesCacheEvent.flameStrikes.isEmpty()) {
            for (UUID flameStrikeUUID : EntitiesCacheEvent.flameStrikes) {
                Flame_Strike_Entity flameStrikeEntity = (Flame_Strike_Entity) ((ServerLevel) level).getEntity(flameStrikeUUID);
                if (flameStrikeEntity == null || flameStrikeEntity.isWaiting()) return;
                if (flameStrikeEntity.getBoundingBox().contains(entity.position())) ((IFlameStrikeEntity)flameStrikeEntity).damagePublic(entity);
            }
        }
    }
}

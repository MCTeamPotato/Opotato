package com.teampotato.opotato.api;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public interface IBehavior<E extends LivingEntity> {
    boolean _canStillUse(ServerLevel level, E entity, long gameTime);
}

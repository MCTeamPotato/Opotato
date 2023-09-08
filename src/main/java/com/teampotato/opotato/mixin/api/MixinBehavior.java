package com.teampotato.opotato.mixin.api;

import com.teampotato.opotato.api.IBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Behavior.class)
public abstract class MixinBehavior<E extends LivingEntity> implements IBehavior<E> {
    @Shadow protected abstract boolean canStillUse(ServerLevel level, E entity, long gameTime);

    @Override
    public boolean _canStillUse(ServerLevel level, E entity, long gameTime) {
        return this.canStillUse(level, entity, gameTime);
    }
}

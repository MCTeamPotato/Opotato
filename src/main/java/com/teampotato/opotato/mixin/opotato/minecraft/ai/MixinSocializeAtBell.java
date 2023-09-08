package com.teampotato.opotato.mixin.opotato.minecraft.ai;

import com.teampotato.opotato.api.IEntityTracker;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.SocializeAtBell;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Collections;

@Mixin(SocializeAtBell.class)
public abstract class MixinSocializeAtBell {
    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    protected void start(ServerLevel level, @NotNull LivingEntity entity, long gameTime) {
        Brain<?> brain = entity.getBrain();
        for (LivingEntity livingEntity : brain.getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(Collections.emptyList())) {
            if (EntityType.VILLAGER.equals(livingEntity.getType()) && livingEntity.distanceToSqr(entity) <= 32.0) {
                brain.setMemory(MemoryModuleType.INTERACTION_TARGET, livingEntity);
                EntityTracker entityTracker = new EntityTracker(livingEntity, true);
                brain.setMemory(MemoryModuleType.LOOK_TARGET, entityTracker);
                ((IEntityTracker)entityTracker).setTrackEyeHeight(false);
                brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(entityTracker, 0.3F, 1));
                break;
            }
        }
    }
}

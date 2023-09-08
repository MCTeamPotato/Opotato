package com.teampotato.opotato.mixin.opotato.minecraft.ai;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;
import java.util.function.Predicate;

@Mixin(SetEntityLookTarget.class)
public abstract class MixinSetEntityLookTarget {
    @Shadow @Final private Predicate<LivingEntity> predicate;

    @Shadow @Final private float maxDistSqr;

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    protected void start(ServerLevel level, @NotNull LivingEntity entity, long gameTime) {
        Brain<?> brain = entity.getBrain();
        for (LivingEntity livingEntity : brain.getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(Collections.emptyList())) {
            if (this.predicate.test(livingEntity) && entity.distanceToSqr(livingEntity) <= (double) this.maxDistSqr) {
                brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(livingEntity, true));
                break;
            }
        }
    }
}

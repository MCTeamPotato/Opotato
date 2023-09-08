package com.teampotato.opotato.mixin.opotato.minecraft.ai;

import com.teampotato.opotato.api.IEntityTracker;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.InteractWith;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;
import java.util.Map;
import java.util.function.Predicate;

@Mixin(InteractWith.class)
public abstract class MixinInteractWith<E extends LivingEntity, T extends LivingEntity> extends Behavior<E> {
    @Shadow @Final private int interactionRangeSqr;
    @Shadow @Final private EntityType<? extends T> type;
    @Shadow @Final private float speedModifier;
    @Shadow @Final private int maxDist;
    @Shadow @Final private Predicate<T> targetFilter;
    @Shadow @Final private MemoryModuleType<T> memory;
    public MixinInteractWith(Map<MemoryModuleType<?>, MemoryStatus> map) {
        super(map);
    }

    /**
     * @author Kasualix
     * @reason avoid stream and allocation
     */
    @Overwrite
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void start(ServerLevel level, @NotNull E entity, long gameTime) {
        Brain<?> brain = entity.getBrain();
        for (LivingEntity targetEntity : brain.getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(Collections.emptyList())) {
            if (this.type.equals(targetEntity.getType()) && targetEntity.distanceToSqr(entity) <= (double) this.interactionRangeSqr && this.targetFilter.test((T) targetEntity)) {
                brain.setMemory((MemoryModuleType) this.memory, targetEntity);
                EntityTracker entityTracker = new EntityTracker(targetEntity, true);
                brain.setMemory(MemoryModuleType.LOOK_TARGET, entityTracker);
                ((IEntityTracker) entityTracker).setTrackEyeHeight(false);
                brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(entityTracker, this.speedModifier, this.maxDist));
                break;
            }
        }
    }
}

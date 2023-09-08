package com.teampotato.opotato.mixin.opotato.minecraft.ai;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.SetLookAndInteract;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;
import java.util.Map;

@Mixin(SetLookAndInteract.class)
public abstract class MixinSetLookAndInteract extends Behavior<LivingEntity> {
    @Shadow protected abstract boolean isMatchingTarget(LivingEntity livingEntity);
    @Shadow @Final private int interactionRangeSqr;
    public MixinSetLookAndInteract(Map<MemoryModuleType<?>, MemoryStatus> map) {
        super(map);
    }

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    public void start(ServerLevel level, LivingEntity entity, long gameTime) {
        super.start(level, entity, gameTime);
        Brain<?> brain = entity.getBrain();
        for (LivingEntity targetEntity : brain.getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(Collections.emptyList())) {
            if (entity.distanceToSqr(targetEntity) <= (double) this.interactionRangeSqr && isMatchingTarget(targetEntity)) {
                brain.setMemory(MemoryModuleType.INTERACTION_TARGET, targetEntity);
                brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(targetEntity, true));
                break;
            }
        }
    }
}

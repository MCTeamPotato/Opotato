package com.teampotato.opotato.mixin.opotato.minecraft.ai;

import com.teampotato.opotato.api.IBehavior;
import com.teampotato.opotato.api.IWeightedList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.GateBehavior;
import net.minecraft.world.entity.ai.behavior.WeightedList;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.Set;

@Mixin(GateBehavior.class)
@SuppressWarnings("unchecked")
public abstract class MixinGateBehavior<E extends LivingEntity> extends Behavior<E> {
    @Shadow @Final private WeightedList<Behavior<? super E>> behaviors;

    @Shadow @Final private Set<MemoryModuleType<?>> exitErasedMemories;

    public MixinGateBehavior(Map<MemoryModuleType<?>, MemoryStatus> map) {
        super(map);
    }

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    protected boolean canStillUse(ServerLevel level, E entity, long gameTime) {
        for (WeightedList.WeightedEntry<Behavior<? super E>> behaviorWeightedEntry : ((IWeightedList<Behavior<? super E>>)this.behaviors).getEntries()) {
            Behavior<? super E> behavior = behaviorWeightedEntry.getData();
            if (behavior.getStatus() == Status.RUNNING && ((IBehavior<? super E>) behavior)._canStillUse(level, entity, gameTime)) return true;
        }
        return false;
    }

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    protected void tick(ServerLevel level, E owner, long gameTime) {
        for (WeightedList.WeightedEntry<Behavior<? super E>> behaviorWeightedEntry : ((IWeightedList<Behavior<? super E>>)this.behaviors).getEntries()) {
            Behavior<? super E> behavior = behaviorWeightedEntry.getData();
            if (behavior.getStatus() == Status.RUNNING) behavior.tickOrStop(level, owner, gameTime);
        }
    }

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    protected void stop(ServerLevel level, E entity, long gameTime) {
        for (WeightedList.WeightedEntry<Behavior<? super E>> behaviorWeightedEntry : ((IWeightedList<Behavior<? super E>>)this.behaviors).getEntries()) {
            Behavior<? super E> behavior = behaviorWeightedEntry.getData();
            if (behavior.getStatus() == Status.RUNNING) behavior.doStop(level, entity, gameTime);
        }
        for (MemoryModuleType<?> memoryModuleType : this.exitErasedMemories) entity.getBrain().eraseMemory(memoryModuleType);
    }
}

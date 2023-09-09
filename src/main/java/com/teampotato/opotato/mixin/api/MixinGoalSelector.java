package com.teampotato.opotato.mixin.api;

import com.teampotato.opotato.api.IGoalSelector;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(GoalSelector.class)
public abstract class MixinGoalSelector implements IGoalSelector {
    @Mutable @Shadow @Final private Set<WrappedGoal> availableGoals;

    @Override
    public Set<WrappedGoal> _getAvailableGoals() {
        return this.availableGoals;
    }
}

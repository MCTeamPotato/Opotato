package com.teampotato.opotato.api;

import net.minecraft.world.entity.ai.goal.WrappedGoal;

import java.util.Set;

public interface IGoalSelector {
    Set<WrappedGoal> potato$getAvailableGoals();
}

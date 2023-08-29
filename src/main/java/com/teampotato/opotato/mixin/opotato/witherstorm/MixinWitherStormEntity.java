package com.teampotato.opotato.mixin.opotato.witherstorm;

import com.teampotato.opotato.api.IGoalSelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.goal.LookAtFormidibombGoal;
import nonamecrackers2.witherstormmod.common.util.PlayDeadManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WitherStormEntity.class)
public abstract class MixinWitherStormEntity extends Monster {
    @Unique
    private static final PlayDeadManager.State[] PLAY_DEAD_MANAGER_STATES = PlayDeadManager.State.values();

    protected MixinWitherStormEntity(EntityType<? extends Monster> arg, Level arg2) {
        super(arg, arg2);
    }

    @Redirect(method = "readAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnonamecrackers2/witherstormmod/common/util/PlayDeadManager$State;values()[Lnonamecrackers2/witherstormmod/common/util/PlayDeadManager$State;", remap = false))
    private PlayDeadManager.State[] avoidAllocation() {
        return PLAY_DEAD_MANAGER_STATES;
    }

    /**
     * @author Kasualix
     * @reason avoid stream and optimize
     */
    @Overwrite(remap = false)
    public boolean isAttractingFormidibomb() {
        for (WrappedGoal prioritizedGoal : ((IGoalSelector)this.goalSelector).potato$getAvailableGoals()) {
            if (prioritizedGoal.isRunning() && prioritizedGoal.getGoal() instanceof LookAtFormidibombGoal && ((LookAtFormidibombGoal) prioritizedGoal.getGoal()).hasTarget()) return true;
        }
        return false;
    }
}

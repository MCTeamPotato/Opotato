package com.teampotato.opotato.mixin.opotato.witherstorm;

import com.teampotato.opotato.api.IEntity;
import com.teampotato.opotato.api.IGoalSelector;
import com.teampotato.opotato.api.UnupdatableInWaterEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.goal.LookAtFormidibombGoal;
import nonamecrackers2.witherstormmod.common.util.PlayDeadManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherStormEntity.class)
public abstract class MixinWitherStormEntity extends Monster implements UnupdatableInWaterEntity {
    @Shadow(remap = false) public abstract boolean shouldTrackUltimateTarget();

    @Shadow(remap = false) public abstract int getPhase();

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

    @Inject(method = "tick", at = @At("RETURN"))
    private void onTick(CallbackInfo ci) {
        if (((IEntity)this).shouldMove() && !this.shouldTrackUltimateTarget() && this.getPhase() > 3) ((IEntity)this).setShouldMove(false);
    }
}

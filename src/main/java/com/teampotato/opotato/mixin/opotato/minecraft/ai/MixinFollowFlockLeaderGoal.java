package com.teampotato.opotato.mixin.opotato.minecraft.ai;

import com.teampotato.opotato.api.entity.IAbstractSchoolingFish;
import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FollowFlockLeaderGoal.class)
public abstract class MixinFollowFlockLeaderGoal extends Goal {
    @Shadow @Final private AbstractSchoolingFish mob;
    @Shadow private int nextStartTick;
    @Shadow protected abstract int nextStartTick(AbstractSchoolingFish taskOwner);

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    public boolean canUse() {
        if (this.mob.hasFollowers()) {
            return false;
        } else if (this.mob.isFollower()) {
            return true;
        } else if (this.nextStartTick > 0) {
            --this.nextStartTick;
            return false;
        } else {
            this.nextStartTick = this.nextStartTick(this.mob);
            Iterable<AbstractSchoolingFish> followers = this.mob.level.getEntitiesOfClass(
                    this.mob.getClass(),
                    this.mob.getBoundingBox().inflate(8.0, 8.0, 8.0),
                    (fish) -> fish.canBeFollowed() || !fish.isFollower()
            );

            for (AbstractSchoolingFish fish : followers) {
                if (fish.canBeFollowed()) {
                    if (!fish.isFollower()) ((IAbstractSchoolingFish) this.mob).addFollowers(followers);
                    return this.mob.isFollower();
                }
            }

            return this.mob.isFollower();
        }
    }
}

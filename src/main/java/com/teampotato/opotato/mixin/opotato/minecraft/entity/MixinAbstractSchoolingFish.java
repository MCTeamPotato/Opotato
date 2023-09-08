package com.teampotato.opotato.mixin.opotato.minecraft.entity;

import com.teampotato.opotato.api.entity.IAbstractSchoolingFish;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractSchoolingFish.class)
public abstract class MixinAbstractSchoolingFish implements IAbstractSchoolingFish {
    @Shadow public int schoolSize;

    @Shadow public abstract int getMaxSchoolSize();

    @Override
    public void addFollowers(Iterable<AbstractSchoolingFish> followers) {
        int maxToAdd = this.getMaxSchoolSize() - this.schoolSize;
        AbstractSchoolingFish fish = (AbstractSchoolingFish) (Object)this;

        for (AbstractSchoolingFish follower : followers) {
            if (maxToAdd <= 0) break;
            if (follower != fish) {
                follower.startFollowing(fish);
                maxToAdd--;
            }
        }
    }
}

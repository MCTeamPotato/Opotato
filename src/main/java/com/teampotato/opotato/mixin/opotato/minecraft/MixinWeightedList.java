package com.teampotato.opotato.mixin.opotato.minecraft;

import com.teampotato.opotato.api.IWeightedList;
import net.minecraft.world.entity.ai.behavior.WeightedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Random;

@Mixin(WeightedList.class)
public abstract class MixinWeightedList<U> {
    @Shadow public abstract WeightedList<U> shuffle(Random random);

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    @SuppressWarnings("unchecked")
    public U getOne(Random random) {
        List<WeightedList.WeightedEntry<U>> shuffledList = ((IWeightedList<U>)this.shuffle(random)).getEntries();

        if (!shuffledList.isEmpty()) {
            return shuffledList.get(0).getData();
        } else {
            throw new RuntimeException();
        }
    }
}

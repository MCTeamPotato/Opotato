package com.teampotato.opotato.mixin.api;

import com.teampotato.opotato.api.IWeightedList;
import net.minecraft.world.entity.ai.behavior.WeightedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(WeightedList.class)
public abstract class MixinWeightedList<U> implements IWeightedList<U> {
    @Shadow @Final protected List<WeightedList.WeightedEntry<U>> entries;

    @Override
    public List<WeightedList.WeightedEntry<U>> getEntries() {
        return this.entries;
    }
}

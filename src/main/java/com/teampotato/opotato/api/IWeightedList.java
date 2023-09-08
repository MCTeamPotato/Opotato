package com.teampotato.opotato.api;

import net.minecraft.world.entity.ai.behavior.WeightedList;

import java.util.List;

public interface IWeightedList<U> {
    List<WeightedList.WeightedEntry<U>> getEntries();
}

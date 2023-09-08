package com.teampotato.opotato.api.entity;

import net.minecraft.world.entity.animal.AbstractSchoolingFish;

public interface IAbstractSchoolingFish {
    void addFollowers(Iterable<AbstractSchoolingFish> followers);
}

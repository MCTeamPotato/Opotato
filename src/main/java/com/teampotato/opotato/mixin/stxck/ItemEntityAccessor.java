package com.teampotato.opotato.mixin.stxck;

import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemEntity.class)
public interface ItemEntityAccessor {
    @Accessor
    int getPickupDelay();

    @Accessor @Mutable
    void setPickupDelay(int pickupDelay);

    @Accessor
    int getAge();

    @Accessor @Mutable
    void setAge(int age);
}

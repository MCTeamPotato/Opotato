package com.teampotato.opotato.mixin.api;

import com.teampotato.opotato.api.IItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Item.class)
public class MixinItem implements IItem {
    @Mutable @Shadow @Final private int maxDamage;

    @Override
    public void _setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }
}

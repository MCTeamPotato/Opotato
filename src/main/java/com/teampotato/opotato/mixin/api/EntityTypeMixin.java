package com.teampotato.opotato.mixin.api;

import com.teampotato.opotato.api.entity.NeatConfigChecker;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityType.class)
public class EntityTypeMixin implements NeatConfigChecker {
    @Unique private boolean potato$isInNeatBlacklist;

    @Override
    public boolean potato$isInNeatBlacklist() {
        return this.potato$isInNeatBlacklist;
    }

    @Override
    public void potato$setIsInNeatBlacklist(boolean isInNeatBlacklist) {
        this.potato$isInNeatBlacklist = isInNeatBlacklist;
    }
}

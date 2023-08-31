package com.teampotato.opotato.mixin.opotato.witherstorm;

import com.teampotato.opotato.api.UnupdatableInWaterEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockClusterEntity.class)
public abstract class MixinBlockClusterEntity extends Entity implements UnupdatableInWaterEntity {
    public MixinBlockClusterEntity(EntityType<?> arg, Level arg2) {
        super(arg, arg2);
    }
}

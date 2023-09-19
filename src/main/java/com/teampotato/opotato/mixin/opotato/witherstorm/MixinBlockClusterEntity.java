package com.teampotato.opotato.mixin.opotato.witherstorm;

import com.teampotato.opotato.api.entity.UnupdatableInWaterEntity;
import nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockClusterEntity.class)
public abstract class MixinBlockClusterEntity implements UnupdatableInWaterEntity {}

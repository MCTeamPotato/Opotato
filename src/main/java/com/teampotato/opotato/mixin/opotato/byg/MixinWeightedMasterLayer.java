package com.teampotato.opotato.mixin.opotato.byg;

import corgiaoc.byg.common.world.dimension.layers.WeightedMasterLayer;
import corgiaoc.byg.util.LayerRandomWeightedListUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.behavior.WeightedList;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.newbiome.context.Context;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WeightedMasterLayer.class)
public abstract class MixinWeightedMasterLayer {
    @Shadow @Final private WeightedList<ResourceLocation> weightedBiomesList;

    /**
     * @author Kasualix
     * @reason give more info on crashing
     */
    @Overwrite
    private int getRandomBiome(@NotNull Registry<Biome> biomeRegistry, Context rand) {
        ResourceLocation id = LayerRandomWeightedListUtil.getBiomeFromID(this.weightedBiomesList, rand);
        return biomeRegistry.getId(biomeRegistry.getOptional(id)
                .orElseThrow(() -> new RuntimeException("Failed to getRandomBiome from ResourceLocation " + id)));
    }
}

package com.teampotato.opotato.mixin.opotato.byg;

import com.teampotato.opotato.EarlySetupInitializer;
import corgiaoc.byg.common.world.biome.BYGNetherBiome;
import corgiaoc.byg.common.world.dimension.layers.WeightedMasterLayer;
import corgiaoc.byg.util.LayerRandomWeightedListUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.behavior.WeightedList;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.newbiome.context.Context;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/**
 * Pretty stupid patch for the RuntimeException fix
 * Because we are crashing when entering the nether, let's get one random biome from all the BYG nether biomes
 **/
@Mixin(WeightedMasterLayer.class)
public abstract class MixinWeightedMasterLayer {
    @Shadow @Final private WeightedList<ResourceLocation> weightedBiomesList;

    @Shadow @Final private Registry<Biome> biomeRegistry;
    @Unique private Supplier<Integer> opotato$randomBiomeId;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(Registry<Biome> biomeRegistry, WeightedList<ResourceLocation> locationWeightedList, CallbackInfo ci) {
        this.opotato$randomBiomeId = () -> {
            try {
                return this.biomeRegistry.getId(BYGNetherBiome.BYG_NETHER_BIOMES.get(ThreadLocalRandom.current().nextInt(BYGNetherBiome.BYG_NETHER_BIOMES.size())).getBiome());
            } catch (Exception exception) {
                EarlySetupInitializer.LOGGER.error("Error occurs when getting one random biome from BYG nether biomes list. Fallback: use soul sand valley biome id. How??? The nether biomes list is empty???", exception);
                return this.biomeRegistry.getId(ForgeRegistries.BIOMES.getValue(Biomes.SOUL_SAND_VALLEY.getRegistryName()));
            }
        };
    }

    @Inject(method = "getRandomBiome", at = @At("HEAD"), cancellable = true)
    private void fixRandomCrash(Registry<Biome> biomeRegistry, Context rand, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(biomeRegistry.getOptional(LayerRandomWeightedListUtil.getBiomeFromID(this.weightedBiomesList, rand)).map(biomeRegistry::getId).orElse(opotato$randomBiomeId.get()));
    }
}

package com.teampotato.opotato.mixin.opotato.minecraft.client;

import net.minecraft.client.gui.screens.worldselection.WorldPreset;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(WorldPreset.class)
public abstract class MixinWorldPreset {
    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    private static Biome parseBuffetSettings(RegistryAccess registryAccess, @NotNull WorldGenSettings worldGenSettings) {
        List<Biome> possibleBiomes = worldGenSettings.overworld().getBiomeSource().possibleBiomes();
        if (!possibleBiomes.isEmpty()) {
            return possibleBiomes.get(0);
        } else {
            return registryAccess.registryOrThrow(Registry.BIOME_REGISTRY).getOrThrow(Biomes.PLAINS);
        }
    }
}

package com.teampotato.opotato.mixin.opotato.minecraft;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import org.spongepowered.asm.mixin.*;

import java.util.List;
import java.util.Map;

@Mixin(BiomeSource.class)
public abstract class MixinBiomeSource {
    @Shadow @Final protected Map<StructureFeature<?>, Boolean> supportedStructures;

    @Shadow @Final protected List<Biome> possibleBiomes;

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    public boolean canGenerateStructure(StructureFeature<?> feature) {
        return this.supportedStructures.computeIfAbsent(feature, (structureFeature) -> {
            for (Biome biome : this.possibleBiomes) {
                if (biome.getGenerationSettings().isValidStart(structureFeature)) return true;
            }
            return false;
        });
    }
}

package com.teampotato.opotato.mixin.opotato.minecraft;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DataFixUtils;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Mixin(BiomeGenerationSettings.class)
public abstract class MixinBiomeGenerationSettings {
    @Shadow @Final private List<Supplier<ConfiguredStructureFeature<?, ?>>> structureStarts;

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    public boolean isValidStart(StructureFeature<?> structureFeature) {
        for (Supplier<ConfiguredStructureFeature<?, ?>> featureSupplier : this.structureStarts) {
            if (featureSupplier.get().feature == structureFeature) return true;
        }
        return false;
    }

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    public ConfiguredStructureFeature<?, ?> withBiomeConfig(ConfiguredStructureFeature<?, ?> structure) {
        for (Supplier<ConfiguredStructureFeature<?, ?>> featureSupplier : this.structureStarts) {
            ConfiguredStructureFeature<?, ?> feature = featureSupplier.get();
            if (feature.feature == structure.feature) return DataFixUtils.orElse(Optional.of(feature), structure);
        }
        return structure;
    }

    @Mixin(BiomeGenerationSettings.Builder.class)
    public abstract static class MixinBiomeGenerationSettingsBuilder {

        @Shadow @Final protected Map<GenerationStep.Carving, List<Supplier<ConfiguredWorldCarver<?>>>> carvers;
        @Shadow @Final protected List<List<Supplier<ConfiguredFeature<?, ?>>>> features;
        @Shadow @Final protected List<Supplier<ConfiguredStructureFeature<?, ?>>> structureStarts;
        @SuppressWarnings("OptionalUsedAsFieldOrParameterType") @Shadow protected Optional<Supplier<ConfiguredSurfaceBuilder<?>>> surfaceBuilder;

        /**
         * @author Kasualix
         * @reason avoid stream and other strange allocation
         */
        @Overwrite
        public BiomeGenerationSettings build() {
            return new BiomeGenerationSettings(
                    this.surfaceBuilder.orElseThrow(() -> new IllegalStateException("Missing surface builder")),
                    ImmutableMap.copyOf(this.carvers),
                    ImmutableList.copyOf(this.features),
                    ImmutableList.copyOf(this.structureStarts)
            );
        }
    }
}

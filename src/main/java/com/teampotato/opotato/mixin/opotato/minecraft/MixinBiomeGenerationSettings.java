package com.teampotato.opotato.mixin.opotato.minecraft;

import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
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
            if (featureSupplier.get().feature.equals(structureFeature)) return true;
        }
        return false;
    }
}

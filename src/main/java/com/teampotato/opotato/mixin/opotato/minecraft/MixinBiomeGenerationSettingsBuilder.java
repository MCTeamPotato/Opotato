package com.teampotato.opotato.mixin.opotato.minecraft;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Mixin(BiomeGenerationSettings.Builder.class)
public abstract class MixinBiomeGenerationSettingsBuilder {

    @Mutable @Shadow @Final protected Map<GenerationStep.Carving, List<Supplier<ConfiguredWorldCarver<?>>>> carvers;

    @Mutable @Shadow @Final protected List<List<Supplier<ConfiguredFeature<?, ?>>>> features;

    @Mutable @Shadow @Final protected List<Supplier<ConfiguredStructureFeature<?, ?>>> structureStarts;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Shadow protected Optional<Supplier<ConfiguredSurfaceBuilder<?>>> surfaceBuilder;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        this.carvers = new Object2ObjectLinkedOpenHashMap<>(this.carvers);
        this.features = new ObjectArrayList<>(this.features);
        this.structureStarts = new ObjectArrayList<>(this.structureStarts);
    }

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

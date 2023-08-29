package com.teampotato.opotato.mixin.opotato.minecraft;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(BiomeSource.class)
public abstract class MixinBiomeSource {
    @Mutable @Shadow @Final protected Map<StructureFeature<?>, Boolean> supportedStructures;

    @Mutable @Shadow @Final protected Set<BlockState> surfaceBlocks;

    @Mutable @Shadow @Final protected List<Biome> possibleBiomes;

    @Inject(method = "<init>(Ljava/util/List;)V", at = @At("RETURN"))
    private void onInit(List<Biome> list, CallbackInfo ci) {
        this.possibleBiomes = new ObjectArrayList<>(this.possibleBiomes);
        this.supportedStructures = new Object2ObjectOpenHashMap<>(this.supportedStructures);
        this.surfaceBlocks = new ObjectOpenHashSet<>(this.surfaceBlocks);
    }

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

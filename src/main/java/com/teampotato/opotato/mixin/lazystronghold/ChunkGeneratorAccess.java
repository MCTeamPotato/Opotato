package com.teampotato.opotato.mixin.lazystronghold;

import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkGenerator.class)
public interface ChunkGeneratorAccess {
    @Invoker
    void invokeGenerateStrongholds();

    @Accessor
    BiomeProvider getBiomeSource();
}

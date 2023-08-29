package com.teampotato.opotato.mixin.opotato.minecraft;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.StructureSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChunkGenerator.class)
public abstract class MixinChunkGenerator {
    @Mutable @Shadow @Final private List<ChunkPos> strongholdPositions;

    @Inject(method = "<init>(Lnet/minecraft/world/level/biome/BiomeSource;Lnet/minecraft/world/level/biome/BiomeSource;Lnet/minecraft/world/level/levelgen/StructureSettings;J)V", at = @At("RETURN"))
    private void onInit(BiomeSource arg, BiomeSource arg2, StructureSettings arg3, long l, CallbackInfo ci) {
        this.strongholdPositions = new ObjectArrayList<>(this.strongholdPositions);
    }
}

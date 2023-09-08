package com.teampotato.opotato.mixin.opotato.minecraft;

import com.teampotato.opotato.api.IStructureFeatureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.Random;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Mixin(LakeFeature.class)
public abstract class MixinLakeFeature {
    @Inject(method = "place(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/feature/configurations/BlockStateConfiguration;)Z", at = @At(value = "INVOKE", target = "Ljava/util/Optional;isPresent()Z", remap = false, shift = At.Shift.BEFORE), cancellable = true)
    private void onPlace(WorldGenLevel worldGenLevel, ChunkGenerator chunkGenerator, Random random, BlockPos pos, BlockStateConfiguration configuration, CallbackInfoReturnable<Boolean> cir) {
        StructureFeatureManager manager = worldGenLevel.getLevel().structureFeatureManager();
        ServerLevel serverLevel = worldGenLevel.getLevel();
        for (long longPos : serverLevel.getChunk(pos.getX() >> 4, pos.getZ() >> 4, ChunkStatus.STRUCTURE_REFERENCES).getReferencesForFeature(StructureFeature.VILLAGE)) {
            StructureStart<?> start = ((IStructureFeatureManager)manager).getStartForFeature(StructureFeature.VILLAGE, serverLevel.getChunk((int) longPos, (int) (longPos >> 32), ChunkStatus.STRUCTURE_STARTS));

            if (start != null && start.isValid()) {
                cir.setReturnValue(false);
                cir.cancel();
                break;
            }
        }
    }

    @Redirect(method = "place(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/feature/configurations/BlockStateConfiguration;)Z", at = @At(value = "INVOKE", target = "Ljava/util/Optional;isPresent()Z"))
    private boolean onCheckPresent(Optional<?> instance) {
        return false;
    }
}

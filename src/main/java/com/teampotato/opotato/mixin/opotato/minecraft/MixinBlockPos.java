package com.teampotato.opotato.mixin.opotato.minecraft;

import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(BlockPos.class)
public abstract class MixinBlockPos {
    @Shadow public static Iterable<BlockPos> withinManhattan(BlockPos pos, int xSize, int ySize, int zSize) {
        throw new RuntimeException();
    }

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    public static Optional<BlockPos> findClosestMatch(BlockPos pos, int width, int height, Predicate<BlockPos> posFilter) {
        for (BlockPos blockPos : withinManhattan(pos, width, height, width)) {
            if (posFilter.test(blockPos)) return Optional.of(blockPos);
        }
        return Optional.empty();
    }
}

package com.teampotato.opotato.mixin.opotato.minecraft;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.StructureUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Collection;
import java.util.Optional;

@Mixin(StructureUtils.class)
public abstract class MixinStructureUtils {

    @Unique
    private static final Int2ObjectOpenHashMap<Rotation> rotationMap = new Int2ObjectOpenHashMap<>();

    static {
        rotationMap.put(0, Rotation.NONE);
        rotationMap.put(1, Rotation.CLOCKWISE_90);
        rotationMap.put(2, Rotation.CLOCKWISE_180);
        rotationMap.put(3, Rotation.COUNTERCLOCKWISE_90);
    }

    /**
     * @author Kasualix
     * @reason use faster map impl
     */
    @Overwrite
    public static Rotation getRotationForRotationSteps(int rotationSteps) {
        return rotationMap.get(rotationSteps);
    }

    @Shadow public static Collection<BlockPos> findStructureBlocks(BlockPos pos, int i, ServerLevel serverLevel) {
        throw new RuntimeException();
    }

    @Shadow
    private static boolean doesStructureContain(BlockPos structureBlockPos, BlockPos posToTest, ServerLevel serverLevel) {
        throw new RuntimeException();
    }

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    public static Optional<BlockPos> findStructureBlockContainingPos(BlockPos pos, int i, ServerLevel serverLevel) {
        for (BlockPos structurePos : findStructureBlocks(pos, i, serverLevel)) {
            if (doesStructureContain(structurePos, pos, serverLevel)) return Optional.of(structurePos);
        }
        return Optional.empty();
    }
}

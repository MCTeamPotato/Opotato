package com.teampotato.opotato.util.alternatecurrent.wire;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraftforge.common.util.Constants;

public class LevelHelper {

    /**
     * An optimized version of {@link net.minecraft.world.level.Level#setBlock
     * Level.setBlock}. Since this method is only used to update redstone wire block
     * states, lighting checks, height map updates, and block entity updates are
     * omitted.
     */
    static boolean setWireState(ServerLevel level, BlockPos pos, BlockState state, boolean updateNeighborShapes) {
        int y = pos.getY();

        if (y < 0 || y >= level.getMaxBuildHeight()) return false;

        int x = pos.getX();
        int z = pos.getZ();
        int index = y >> 4;

        ChunkAccess chunk = level.getChunk(x >> 4, z >> 4, ChunkStatus.FULL, true);
        assert chunk != null;
        LevelChunkSection section = chunk.getSections()[index];

        if (section == null) return false;


        BlockState prevState = section.setBlockState(x & 15, y & 15, z & 15, state);

        if (state == prevState) return false;


        // notify clients of the BlockState change
        level.getChunkSource().blockChanged(pos);
        // mark the chunk for saving
        chunk.setUnsaved(true);

        if (updateNeighborShapes) {
            prevState.updateIndirectNeighbourShapes(level, pos, Constants.BlockFlags.DEFAULT);
            state.updateNeighbourShapes(level, pos, Constants.BlockFlags.DEFAULT);
            state.updateIndirectNeighbourShapes(level, pos, Constants.BlockFlags.DEFAULT);
        }
        return true;
    }
}

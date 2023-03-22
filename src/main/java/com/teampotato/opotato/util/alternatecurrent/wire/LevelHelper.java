package com.teampotato.opotato.util.alternatecurrent.wire;


import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

public class LevelHelper {

	/**
	 * An optimized version of {@link net.minecraft.world.World#setBlock
	 * Level.setBlock}. Since this method is only used to update redstone wire block
	 * states, lighting checks, height map updates, and block entity updates are
	 * omitted.
	 */
	static boolean setWireState(ServerWorld level, BlockPos pos, BlockState state, boolean updateNeighborShapes) {
		int y = pos.getY();

		if (y < 0 || y >= level.getMaxBuildHeight()) {
			return false;
		}

		int x = pos.getX();
		int z = pos.getZ();

		IChunk chunk = level.getChunk(x >> 4, z >> 4, ChunkStatus.FULL, true);
		ChunkSection section = null;
		if (chunk != null) {
			section = chunk.getSections()[y >> 4];
		}

		if (section == null) {
			return false; // we should never get here
		}

		BlockState prevState = section.setBlockState(x & 15, y & 15, z & 15, state);

		if (state == prevState) {
			return false;
		}

		// notify clients of the BlockState change
		level.getChunkSource().blockChanged(pos);
		// mark the chunk for saving
		chunk.setUnsaved(true);

		if (updateNeighborShapes) {
			prevState.updateIndirectNeighbourShapes(level, pos, Constants.BlockFlags.BLOCK_UPDATE);
			state.updateNeighbourShapes(level, pos, Constants.BlockFlags.BLOCK_UPDATE);
			state.updateIndirectNeighbourShapes(level, pos, Constants.BlockFlags.BLOCK_UPDATE);
		}

		return true;
	}
}

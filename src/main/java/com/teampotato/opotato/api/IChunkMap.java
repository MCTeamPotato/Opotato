package com.teampotato.opotato.api;

import net.minecraft.world.level.ChunkPos;

public interface IChunkMap {
    boolean potato$hasNoPlayers(ChunkPos pos, boolean boundaryOnly);
}

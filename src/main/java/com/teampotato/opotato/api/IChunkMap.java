package com.teampotato.opotato.api;

import net.minecraft.world.level.ChunkPos;

public interface IChunkMap {
    boolean hasNoPlayers(ChunkPos pos, boolean boundaryOnly);
}

package com.teampotato.opotato.api;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;

import java.util.List;

public interface IChunkMap {
    boolean hasNotPlayers(ChunkPos pos, boolean boundaryOnly);

    List<ServerPlayer> getPlayerSet(ChunkPos pos, boolean boundaryOnly);
}

package com.teampotato.opotato.util.schwarz.chunk;

import net.minecraft.world.chunk.Chunk;

import java.util.concurrent.CopyOnWriteArrayList;

public class ChunkData {
    public static CopyOnWriteArrayList<Chunk> loadedChunks = new CopyOnWriteArrayList<>();
}

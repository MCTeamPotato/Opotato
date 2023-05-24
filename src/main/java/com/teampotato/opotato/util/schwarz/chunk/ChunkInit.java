package com.teampotato.opotato.util.schwarz.chunk;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CopyOnWriteArrayList;

@Mod.EventBusSubscriber
public class ChunkInit {
    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        ChunkData.loadedChunks.add((Chunk) event.getChunk());
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload event) {
        CopyOnWriteArrayList<Chunk> loadedChunks = ChunkData.loadedChunks;
        loadedChunks.removeIf(loadedChunk -> loadedChunk.getPos().equals(event.getChunk().getPos()));
        ChunkData.loadedChunks = loadedChunks;
    }
}

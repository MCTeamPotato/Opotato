package com.teampotato.opotato.util.schwarz;

import com.teampotato.opotato.Opotato;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CopyOnWriteArrayList;

@Mod.EventBusSubscriber
public class ChunkInit {

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        Opotato.loadedChunks.add((Chunk) event.getChunk());
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload event) {
        CopyOnWriteArrayList<Chunk> loadedChunks = Opotato.loadedChunks;
        loadedChunks.removeIf(loadedchunk -> loadedchunk.getPos().equals(event.getChunk().getPos()));
        Opotato.loadedChunks = loadedChunks;
    }
}

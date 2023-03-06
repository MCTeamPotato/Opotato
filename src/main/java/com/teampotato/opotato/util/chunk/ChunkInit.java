package com.teampotato.opotato.util.chunk;

import com.teampotato.opotato.Opotato;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class ChunkInit {

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        Opotato.loadedChunks.add((LevelChunk) event.getChunk());
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload event) {
        List<LevelChunk> loadedChunks = Opotato.loadedChunks;
        loadedChunks.removeIf(loadedchunk -> loadedchunk.getPos().equals(event.getChunk().getPos()));
        Opotato.loadedChunks = loadedChunks;
    }
}

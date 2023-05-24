package com.teampotato.opotato.event;

import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.util.schwarz.command.Command;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CopyOnWriteArrayList;

@Mod.EventBusSubscriber(modid = Opotato.ID)
public class SchwarzEvents {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        Command.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        Opotato.loadedChunks.add((Chunk) event.getChunk());
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload event) {
        CopyOnWriteArrayList<Chunk> loadedChunks = Opotato.loadedChunks;
        loadedChunks.removeIf(loadedChunk -> loadedChunk.getPos().equals(event.getChunk().getPos()));
        Opotato.loadedChunks = loadedChunks;
    }
}

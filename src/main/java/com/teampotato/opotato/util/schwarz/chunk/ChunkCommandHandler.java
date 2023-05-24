package com.teampotato.opotato.util.schwarz.chunk;

import com.teampotato.opotato.Opotato;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.chunk.Chunk;

import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChunkCommandHandler {
    private static CopyOnWriteArrayList<ChunkStatus> scores = new CopyOnWriteArrayList<>();

    public static void analyseChunk(CommandSource serverCommandSource) {
        CopyOnWriteArrayList<Chunk> currentWorldChunkList = Opotato.loadedChunks;
        scores.clear();
        currentWorldChunkList.forEach(chunk -> scores.add(new ChunkStatus(chunk)));
        scores.forEach(ChunkStatus::genScore);
        Collections.sort(scores);
        serverCommandSource.sendSuccess(new StringTextComponent("Chunk Analyse Report \n"), false);
        for (int i = 0; i < 10; i++) {
            if (i >= scores.size()) break;
            ChunkStatus chunkStatus = scores.get(i);
            serverCommandSource.sendSuccess(chunkStatus.genText(), false);
        }
    }
}

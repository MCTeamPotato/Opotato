package com.teampotato.opotato.util.schwarz;

import com.teampotato.opotato.Opotato;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChunkCommandHandler {
    private static final List<ChunkStatus> scores = new ArrayList<>();

    public static void AnalyseChunk(CommandSource commandSource) {
        List<Chunk> currentWorldChunkList = Opotato.loadedChunks;
        scores.clear();
        for (Chunk chunk : currentWorldChunkList) {
            scores.add(new ChunkStatus(chunk));
        }

        for (ChunkStatus chunkStatus : scores) {
            chunkStatus.genScore();
        }
        Collections.sort(scores);

        commandSource.sendSuccess(new StringTextComponent("Chunk Analyse Report \n"), false);
        for (int i = 0; i < 10; i++) {
            if (i >= scores.size()) {
                break;
            }
            ChunkStatus chunkStatus = scores.get(i);
            commandSource.sendSuccess(chunkStatus.genText(), false);
        }
    }
}

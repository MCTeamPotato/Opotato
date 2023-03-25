package com.teampotato.opotato;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.teampotato.opotato.util.schwarz.ChunkCommandHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(Opotato.ID)
public class Opotato {

    public static final String ID = "opotato";
    public static final Logger LOGGER = LogManager.getLogger(ID);
    public static List<Chunk> loadedChunks = new ArrayList<>();


    public Opotato() {
        LOGGER.info("I love you.");
    }

    public static class OpotatoCommand {
        public static void register(CommandDispatcher<CommandSource> dispatcher) {

            LiteralArgumentBuilder<CommandSource> schwarz = Commands
                    .literal("schwarz")
                    .then(Commands
                            .literal("chunkanalyse")
                            .executes(ChunkCommandHandler::ChunkAnalyse));

            dispatcher.register(schwarz);
        }
    }
}

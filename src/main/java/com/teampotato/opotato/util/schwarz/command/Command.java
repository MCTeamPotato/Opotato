package com.teampotato.opotato.util.schwarz.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.teampotato.opotato.util.schwarz.chunk.ChunkCommandHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class Command {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> schwarz = Commands.literal("schwarz").then(Commands.literal("chunkanalyse").executes(Command::chunkAnalyse));
        dispatcher.register(schwarz);
    }

    public static int chunkAnalyse(CommandContext<CommandSource> ctx) {
        CommandSource serverCommandSource = ctx.getSource();
        ChunkCommandHandler.analyseChunk(serverCommandSource);
        return 1;
    }
}

package com.teampotato.opotato.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.teampotato.opotato.config.PotatoCommonConfig;
import com.teampotato.opotato.util.schwarz.ChunkCommandHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class OpotatoCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> schwarz = Commands
                .literal("schwarz")
                .then(Commands
                        .literal("chunkanalyse")
                        .executes(OpotatoCommand::ChunkAnalyse));
        dispatcher.register(schwarz);

        if (!PotatoCommonConfig.ENABLE_CHATGPT.get()) return;
    }

    public static int ChunkAnalyse(CommandContext<CommandSource> ctx) {
        CommandSource commandSourceStack = ctx.getSource();
        ChunkCommandHandler.AnalyseChunk(commandSourceStack);
        return 1;
    }
}

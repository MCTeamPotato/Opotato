package com.teampotato.opotato;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.teampotato.opotato.config.PotatoCommonConfig;
import com.teampotato.opotato.util.alternatecurrent.AlternateCurrentCmdUtils;
import com.teampotato.opotato.util.schwarz.ChunkCommandHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class OpotatoCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {

        LiteralArgumentBuilder<CommandSource> alternatecurrent = Commands.
                literal("alternatecurrent").
                requires(source -> source.hasPermission(2)).
                executes(context -> AlternateCurrentCmdUtils.query(context.getSource())).
                then(Commands.
                        literal("on").
                        executes(context -> AlternateCurrentCmdUtils.set(context.getSource(), true))).
                then(Commands.
                        literal("off").
                        executes(context -> AlternateCurrentCmdUtils.set(context.getSource(), false))).
                then(Commands.
                        literal("resetProfiler").
                        requires(source -> PotatoCommonConfig.ALTERNATE_CURRENT_DEBUG_MODE.get()).
                        executes(context -> AlternateCurrentCmdUtils.resetProfiler(context.getSource())));

        LiteralArgumentBuilder<CommandSource> schwarz = Commands
                .literal("schwarz")
                .then(Commands
                        .literal("chunkanalyse")
                        .executes(ChunkCommandHandler::ChunkAnalyse));

        dispatcher.register(schwarz);
        dispatcher.register(alternatecurrent);

        //if (!PotatoCommonConfig.ENABLE_CHATGPT.get()) return;
    }
}

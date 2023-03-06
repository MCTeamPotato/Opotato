package com.teampotato.potatoptimize;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.teampotato.potatoptimize.config.PotatoCommonConfig;
import com.teampotato.potatoptimize.util.chunk.ChunkCommandHandler;
import com.teampotato.potatoptimize.util.profiler.ProfilerResults;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;


public class PotatOptimizeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.
                literal("alternatecurrent").
                requires(source -> source.hasPermission(2)).
                executes(context -> query(context.getSource())).
                then(Commands.
                        literal("on").
                        executes(context -> set(context.getSource(), true))).
                then(Commands.
                        literal("off").
                        executes(context -> set(context.getSource(), false))).
                then(Commands.
                        literal("resetProfiler").
                        requires(source -> PotatoCommonConfig.ALTERNATE_CURRENT_DEBUG_MODE.get()).
                        executes(context -> resetProfiler(context.getSource())));

        LiteralArgumentBuilder<CommandSourceStack> builder2 = Commands.
                literal("schwarz").
                then(Commands.
                        literal("chunkanalyse").
                        executes(PotatOptimizeCommand::ChunkAnalyse));
        dispatcher.register(builder);
        dispatcher.register(builder2);
    }

    private static int query(CommandSourceStack source) {
        String state = PotatOptimize.on ? "enabled" : "disabled";
        source.sendSuccess(Component.nullToEmpty(String.format("Alternate Current is currently %s", state)), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int set(CommandSourceStack source, boolean on) {
        PotatOptimize.on = on;

        String state = PotatOptimize.on ? "enabled" : "disabled";
        source.sendSuccess(Component.nullToEmpty(String.format("Alternate Current has been %s!", state)), true);

        return Command.SINGLE_SUCCESS;
    }

    private static int resetProfiler(CommandSourceStack source) {
        source.sendSuccess(Component.nullToEmpty("profiler results have been cleared!"), true);

        ProfilerResults.log();
        ProfilerResults.clear();

        return Command.SINGLE_SUCCESS;
    }

    public static int ChunkAnalyse(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack commandSourceStack = ctx.getSource();
        ChunkCommandHandler.AnalyseChunk(commandSourceStack);
        return 1;
    }
}

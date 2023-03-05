package com.teampotato.potatoptimize;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.teampotato.potatoptimize.profiler.ProfilerResults;
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
                        requires(source -> PotatOptimize.DEBUG).
                        executes(context -> resetProfiler(context.getSource())));

        dispatcher.register(builder);
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
}

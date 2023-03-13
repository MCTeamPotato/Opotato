package com.teampotato.opotato.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.config.PotatoCommonConfig;
import com.teampotato.opotato.util.ChatGPTUtils;
import com.teampotato.opotato.util.alternatecurrent.profiler.ProfilerResults;
import com.teampotato.opotato.util.schwarz.ChunkCommandHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.util.concurrent.CompletableFuture;

public class OpotatoCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder1 = Commands
                .literal("alternatecurrent")
                .requires(source -> source.hasPermission(2))
                .executes(context -> query(context.getSource()))
                .then(Commands
                        .literal("on")
                        .executes(context -> set(context.getSource(), true)))
                .then(Commands
                        .literal("off")
                        .executes(context -> set(context.getSource(), false)))
                .then(Commands
                        .literal("resetProfiler")
                        .requires(source -> PotatoCommonConfig.ALTERNATE_CURRENT_DEBUG_MODE.get())
                        .executes(context -> resetProfiler(context.getSource())));

        LiteralArgumentBuilder<CommandSourceStack> builder2 = Commands
                .literal("schwarz")
                .then(Commands
                        .literal("chunkanalyse")
                        .executes(OpotatoCommand::ChunkAnalyse));
        dispatcher.register(builder1);
        dispatcher.register(builder2);

        if (!PotatoCommonConfig.ENABLE_CHATGPT.get()) return;
        LiteralArgumentBuilder<CommandSourceStack> builder3 = Commands
                .literal("chatgpt")
                .then(Commands.argument("message", StringArgumentType.greedyString()))
                .executes(context -> {
                    try {
                            String message = StringArgumentType.getString(context, "message");
                            String prompt = ChatGPTUtils.generatePrompt(message);
                            CompletableFuture<String> future = ChatGPTUtils.getChatGPTResponse(prompt);
                            future.thenAcceptAsync(response -> {
                                String playerName = context.getSource().getTextName();
                                context.getSource().sendSuccess(
                                        new TextComponent("[" + playerName + "] -> ")
                                                .withStyle(ChatFormatting.GREEN)
                                                .append(new TextComponent(message).withStyle(ChatFormatting.AQUA)), false
                                );
                                context.getSource().sendSuccess(
                                        new TextComponent("[ChatGPT-" + PotatoCommonConfig.MODEL.get() + "] -> " + "[" + playerName + "]" +": ")
                                                .withStyle(ChatFormatting.GOLD)
                                                .append(new TextComponent(response).withStyle(ChatFormatting.YELLOW)), false
                                );
                            });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return 0;
                });
        dispatcher.register(builder3);
    }

    private static int query(CommandSourceStack source) {
        String state = Opotato.on ? "enabled" : "disabled";
        source.sendSuccess(Component.nullToEmpty(String.format("Alternate Current is currently %s", state)), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int set(CommandSourceStack source, boolean on) {
        Opotato.on = on;

        String state = Opotato.on ? "enabled" : "disabled";
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

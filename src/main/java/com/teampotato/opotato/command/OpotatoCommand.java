package com.teampotato.opotato.command;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.config.PotatoCommonConfig;
import com.teampotato.opotato.util.alternatecurrent.profiler.ProfilerResults;
import com.teampotato.opotato.util.chatgpt.ChatGPTUtils;
import com.teampotato.opotato.util.chatgpt.TomlUtils;
import com.teampotato.opotato.util.schwarz.ChunkCommandHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.teampotato.opotato.util.chatgpt.TomlUtils.MODEL;

public class OpotatoCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> alternatecurrent = Commands
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

        LiteralArgumentBuilder<CommandSourceStack> schwarz = Commands
                .literal("schwarz")
                .then(Commands
                        .literal("chunkanalyse")
                        .executes(OpotatoCommand::ChunkAnalyse));
        dispatcher.register(alternatecurrent);
        dispatcher.register(schwarz);

        if (!PotatoCommonConfig.ENABLE_CHATGPT.get()) return;
        LiteralArgumentBuilder<CommandSourceStack> chatgpt = Commands
                .literal("chatgpt")
                .then(Commands.argument("message", StringArgumentType.greedyString()))
                .executes(context -> {
                    try {
                        String message = StringArgumentType.getString(context, "message");
                        String prompt = ChatGPTUtils.generatePrompt(message);
                        CompletableFuture<String> future = ChatGPTUtils.getChatGPTResponse(prompt);
                        future.thenAcceptAsync(response -> {
                            String playerName = context.getSource().getTextName();
                            context.getSource().sendSuccess(new TextComponent("[" + playerName + "] -> ").withStyle(ChatFormatting.GREEN)
                                    .append(new TextComponent(message).withStyle(ChatFormatting.AQUA)), false);
                            context.getSource().sendSuccess(new TextComponent("[ChatGPT-" + MODEL + "] -> " + "[" + playerName + "]" + ": ").withStyle(ChatFormatting.GOLD)
                                    .append(new TextComponent(response).withStyle(ChatFormatting.YELLOW)), false);
                        });
                        return 1;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0;
                    }
                });
        LiteralArgumentBuilder<CommandSourceStack> mgptCommands = Commands.literal("mgpt")
                .requires(source -> source.hasPermission(2));
        LiteralArgumentBuilder<CommandSourceStack> tokenCommand = Commands.literal("token")
                .then(Commands.argument("token", StringArgumentType.string())
                        .executes(context -> {
                            String apiKey = StringArgumentType.getString(context, "token");
                            if (apiKey.startsWith("sk-")) {
                                Toml toml = TomlUtils.readTomlFromFile(Opotato.CHATGPT_CONFIG);
                                Map<String, Object> tomlMap = toml.toMap();
                                tomlMap.put("api_key", apiKey);
                                TomlWriter writer = new TomlWriter();
                                try {
                                    writer.write(tomlMap, new File(Opotato.CHATGPT_CONFIG));
                                    context.getSource().sendSuccess(new TranslatableComponent("minegpt.set.configapikey"), true);
                                } catch (IOException e) {
                                    context.getSource().sendSuccess(new TranslatableComponent("minegpt.error.saveconfig"), true);
                                    Opotato.LOGGER.error("Failed to save config file: {}", e.getMessage());
                                }
                            } else {
                                context.getSource().sendFailure(new TranslatableComponent("minegpt.error.apikey"));
                            }

                            return 1;
                        })
                );
        mgptCommands.then(Commands.literal("default")
                .executes(context -> {
                    Toml toml = TomlUtils.readTomlFromFile(Opotato.CHATGPT_CONFIG);
                    Map<String, Object> tomlMap = toml.toMap();
                    tomlMap.put("endpoint", "https://api.openai.com/v1/completions");
                    tomlMap.put("model", "text-davinci-003");
                    tomlMap.put("max_tokens", "1024");
                    tomlMap.put("n", "1");
                    TomlWriter writer = new TomlWriter();
                    try {
                        writer.write(toml, new File(Opotato.CHATGPT_CONFIG));
                        context.getSource().sendSuccess(new TranslatableComponent("minegpt.set.default", Opotato.CHATGPT_CONFIG), true);
                    } catch (IOException e) {
                        context.getSource().sendFailure(new TranslatableComponent("minegpt.error.saveconfig"));
                        Opotato.LOGGER.error("Failed to save config file: {}", e.getMessage());
                    }
                    return 1;
                })
        );
        mgptCommands.then(tokenCommand);
        mgptCommands.then(Commands.literal("reload")
                .executes(context -> {
                    TomlUtils.reloadConfig();
                    context.getSource().sendSuccess(new TranslatableComponent("minegpt.reload.config"), true);
                    return 1;
                }));
        dispatcher.register(chatgpt);
        dispatcher.register(mgptCommands);
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

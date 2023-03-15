package com.teampotato.opotato;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.teampotato.opotato.config.PotatoCommonConfig;
import com.teampotato.opotato.util.ChatGPTUtils;
import com.teampotato.opotato.util.alternatecurrent.AlternateCurrentCmdUtils;
import com.teampotato.opotato.util.alternatecurrent.profiler.ACProfiler;
import com.teampotato.opotato.util.alternatecurrent.profiler.Profiler;
import com.teampotato.opotato.util.schwarz.ChunkCommandHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Mod(Opotato.ID)
public class Opotato {

    public static final String ID = "opotato";
    public static final Logger LOGGER = LogManager.getLogger(ID);
    public static List<Chunk> loadedChunks = new ArrayList<>();
    public static boolean on = true;
    public static final String CHAT_GPT_CONFIG = "Opotato_ChatGPT.toml";

    public static Profiler createProfiler() {
        return PotatoCommonConfig.ALTERNATE_CURRENT_DEBUG_MODE.get() ? new ACProfiler() : Profiler.DUMMY;
    }

    public Opotato() {
        String folderPath = "config" + File.separator;
        File folder = new File(folderPath);
        if (!folder.exists()) folder.mkdir();
        String filePath = folderPath + CHAT_GPT_CONFIG;
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("endpoint = \"API_Link\"\n");
                    writer.write("api_key = \"OpenAI_API_Key\"\n");
                    writer.write("model = \"AI_Model\"\n");
                    writer.write("prompt = \"Undeveloped\"\n");
                    writer.write("max_tokens = \"Max_Tokens\"\n");
                    writer.write("n = \"N\"\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PotatoCommonConfig.COMMON_CONFIG);

        ChatGPTUtils.loadChatGPTConfig();
        LOGGER.info("-----------------------------------------");
        LOGGER.info("[Opotato-ChatGPT] Your ChatGPT Config Info:");
        LOGGER.info("-----------------------------------------");
        LOGGER.info("Endpoint: {}", ChatGPTUtils.getEndpoint());
        LOGGER.info("Model: {}", ChatGPTUtils.getModel());
        LOGGER.info("Max tokens: {}", ChatGPTUtils.getMaxTokens());
        LOGGER.info("N: {}", ChatGPTUtils.getN());
        LOGGER.info("-----------------------------------------");
    }

    public static class OpotatoCommand {
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

            if (!PotatoCommonConfig.ENABLE_CHATGPT.get()) return;
            LiteralArgumentBuilder<CommandSource> chatgpt = Commands
                    .literal("chatgpt")
                    .then(Commands.argument("message", StringArgumentType.greedyString()))
                    .executes(context -> {
                        try {
                            String message = StringArgumentType.getString(context, "message");
                            String prompt = ChatGPTUtils.generatePrompt(message);
                            CompletableFuture<String> future = ChatGPTUtils.getChatGPTResponse(prompt);
                            future.thenAcceptAsync(response -> {
                                String playerName = context.getSource().getTextName();
                                context.getSource().sendSuccess(new StringTextComponent("[" + playerName + "] -> ").withStyle(TextFormatting.GREEN)
                                        .append(new StringTextComponent(message).withStyle(TextFormatting.AQUA)), false);
                                context.getSource().sendSuccess(new StringTextComponent("[ChatGPT-" + ChatGPTUtils.getModel() + "] -> " + "[" + playerName + "]" + ": ").withStyle(TextFormatting.GOLD)
                                        .append(new StringTextComponent("\"" + response + "\"").withStyle(TextFormatting.YELLOW)), false);
                            });
                            return 1;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return 0;
                        }
                    });
            dispatcher.register(chatgpt);

            LiteralArgumentBuilder<CommandSource> mgptCommand = Commands.literal("mgpt")
                    .requires(source -> source.hasPermission(2));

            LiteralArgumentBuilder<CommandSource> tokenCommand = Commands.literal("token")
                    .then(Commands.argument("token", StringArgumentType.string())
                            .executes(context -> {
                                String apiKey = StringArgumentType.getString(context, "token");
                                if (ChatGPTUtils.isValidApiKey(apiKey)) {
                                    Toml toml = ChatGPTUtils.readTomlFromFile(CHAT_GPT_CONFIG);
                                    Map<String, Object> tomlMap = toml.toMap();
                                    tomlMap.put("api_key", apiKey);
                                    TomlWriter writer = new TomlWriter();
                                    try {
                                        writer.write(tomlMap, new File(CHAT_GPT_CONFIG));
                                        context.getSource().sendSuccess(new TranslationTextComponent("minegpt.set.configapikey", CHAT_GPT_CONFIG), true);
                                    } catch (IOException e) {
                                        context.getSource().sendSuccess(new TranslationTextComponent("minegpt.error.saveconfig", CHAT_GPT_CONFIG), true);
                                        LOGGER.error("Failed to save config file: {}", e.getMessage());
                                    }
                                } else {
                                    context.getSource().sendFailure(new TranslationTextComponent("minegpt.error.apikey"));
                                }

                                return 1;
                            })
                    );

            mgptCommand.then(Commands.literal("default")
                    .executes(context -> {
                        Toml toml = ChatGPTUtils.readTomlFromFile(CHAT_GPT_CONFIG);
                        Map<String, Object> tomlMap = toml.toMap();
                        tomlMap.put("endpoint", "https://api.openai.com/v1/completions");
                        tomlMap.put("model", "text-davinci-003");
                        tomlMap.put("api_key", "api_key");
                        tomlMap.put("max_tokens", "1024");
                        tomlMap.put("n", "1");
                        try {
                            ChatGPTUtils.writeTomlToFile(tomlMap, CHAT_GPT_CONFIG);
                            context.getSource().sendSuccess(new TranslationTextComponent("minegpt.set.default", CHAT_GPT_CONFIG), true);
                        } catch (IOException e) {
                            context.getSource().sendFailure(new TranslationTextComponent("minegpt.error.saveconfig"));
                            LOGGER.error("Failed to save config file: {}", e.getMessage());
                        }
                        return 1;
                    })
            );

            mgptCommand.then(tokenCommand);

            mgptCommand.then(Commands.literal("reload")
                    .executes(context -> {
                        ChatGPTUtils.reloadConfig();
                        context.getSource().sendSuccess(new TranslationTextComponent("minegpt.reload.config"), true);
                        return 1;
                    }));

            dispatcher.register(mgptCommand);
        }
    }
}

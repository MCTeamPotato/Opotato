package com.teampotato.opotato;

import com.moandjiezana.toml.Toml;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.teampotato.opotato.config.PotatoCommonConfig;
import com.teampotato.opotato.util.alternatecurrent.profiler.ACProfiler;
import com.teampotato.opotato.util.alternatecurrent.profiler.Profiler;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.teampotato.opotato.util.ChatGPTUtils.generatePrompt;
import static com.teampotato.opotato.util.ChatGPTUtils.getChatGPTResponse;

@Mod(Opotato.ID)
public class Opotato {

    public static final String ID = "opotato";
    public static final String NAME = "Opotato";
    public static final String VER = "1.4.0";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static List<LevelChunk> loadedChunks = new ArrayList<>();

    public static boolean on = true;

    public static Profiler creatrProfiler() {
        return PotatoCommonConfig.ALTERNATE_CURRENT_DEBUG_MODE.get() ? new ACProfiler() : Profiler.DUMMY;
    }
    public Opotato() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PotatoCommonConfig.COMMON_CONFIG);
        if(PotatoCommonConfig.ENABLE_CHATGPT.get()){
            net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
                dispatcher.register(Commands.literal("chatgpt").then(Commands.argument("message", StringArgumentType.greedyString())
                    .executes(context -> {
                        try {
                            String message = StringArgumentType.getString(context, "message");
                            String prompt = generatePrompt(message);
                            CompletableFuture<String> future = getChatGPTResponse(prompt);
                            future.thenAcceptAsync(response -> {
                                context.getSource().sendSuccess(new TextComponent(response).withStyle(ChatFormatting.YELLOW), false);
                            });
                            return 1;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }))
                );
            });
        }
    }
}

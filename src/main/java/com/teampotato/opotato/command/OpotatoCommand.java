package com.teampotato.opotato.command;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.config.PotatoCommonConfig;
import com.teampotato.opotato.util.alternatecurrent.profiler.ProfilerResults;
import com.teampotato.opotato.util.schwarz.ChunkCommandHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
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

        if (PotatoCommonConfig.ENABLE_CHATGPT.get()) {

            LiteralArgumentBuilder<CommandSourceStack> builder3 = Commands
                    .literal("chatgpt")
                    .then(Commands.argument("message", StringArgumentType.greedyString()))
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
                    });
            dispatcher.register(builder3);
        }
    }

    private static String generatePrompt(String message) {
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        return "System.out.println(\"" + encodedMessage + "\");\n\npublic class MyFirstProgram {\n    public static void main(String[] args) {\n\n    }\n}";
    }

    private static CompletableFuture<String> getChatGPTResponse(String message) {
        return CompletableFuture.supplyAsync(() -> {
            JSONObject requestData = new JSONObject()
                    .put("model", PotatoCommonConfig.MODEL.get())
                    .put("prompt", "User: " + message + "\nChatGPT:")
                    .put("max_tokens", Integer.parseInt(PotatoCommonConfig.MAX_TOKENS.get()))
                    .put("n", Integer.parseInt(PotatoCommonConfig.N.get()))
                    .put("stop", "\n");
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(PotatoCommonConfig.ENDPOINT.get()))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + PotatoCommonConfig.API_KEY.get())
                        .POST(HttpRequest.BodyPublishers.ofString(requestData.toString()))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonParser parser = new JsonParser();
                JsonObject responseJson = parser.parse(response.body()).getAsJsonObject();
                JsonArray choices = responseJson.getAsJsonArray("choices");
                System.out.println("Received response from ChatGPT API: " + response);
                if (choices == null || choices.size() == 0) return "Failed to get a response from OpenAI API.";
                JsonObject choice = choices.get(0).getAsJsonObject();
                return choice.get("text").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
                return "An error occurred while processing the request.";
            }
        });
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

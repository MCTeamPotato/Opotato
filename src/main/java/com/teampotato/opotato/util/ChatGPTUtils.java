package com.teampotato.opotato.util;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import com.teampotato.opotato.Opotato;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ChatGPTUtils {
    public static final Logger LOGGER = Opotato.LOGGER;

    public static Toml toml;

    private static String ENDPOINT;
    private static String API_KEY;
    private static String MODEL;
    private static String MAX_TOKENS;
    private static String N;

    public static String getEndpoint() {
        return ENDPOINT;
    }

    public static String getApiKey() {
        return API_KEY;
    }

    public static String getModel() {
        return MODEL;
    }

    public static String getMaxTokens() {
        return MAX_TOKENS;
    }

    public static String getN() {
        return N;
    }

    public static void loadChatGPTConfig() {
        toml = readTomlFromFile("config" + File.separator + Opotato.CHAT_GPT_CONFIG);
        ENDPOINT = toml.getString("endpoint");
        API_KEY = toml.getString("api_key");
        MODEL = toml.getString("model");
        MAX_TOKENS = toml.getString("max_tokens");
        N = toml.getString("n");
    }

    public static Toml readTomlFromFile(String path) {
        Toml toml = null;
        try {
            String path2 = "config" + File.separator + "MineGPTconfig.toml";
            InputStream inputStream = Files.newInputStream(new File(path2).toPath());
            toml = new Toml().read(new InputStreamReader(inputStream));
        } catch (Exception e) {
            LOGGER.error("Failed to read file {}", path, e);
        }
        return toml;
    }

    public static void reloadConfig() {
        Toml toml = readTomlFromFile("config" + File.separator + "MineGPTconfig.toml");
        ENDPOINT = toml.getString("endpoint");
        API_KEY = toml.getString("api_key");
        MODEL = toml.getString("model");
        MAX_TOKENS = toml.getString("max_tokens");
        N = toml.getString("n");
    }

    public static void writeTomlToFile(Map<String, Object> map, String path) throws IOException {
        TomlWriter writer = new TomlWriter();
        File file = new File(path);
        Toml toml = new Toml().read(file);
        String API_KEY = toml.getString("API_KEY");
        writer.write(map, file);
        FileWriter fileWriter = new FileWriter(file, true);
        fileWriter.write("\napi_key = \"" + API_KEY + "\"\n");
        fileWriter.close();
    }

    public static String generatePrompt(String message) throws UnsupportedEncodingException {
        String encodedMessage = URLEncoder.encode(message, String.valueOf(StandardCharsets.UTF_8));
        return "System.out.println(\"" + encodedMessage + "\");\n\npublic class MyFirstProgram {\n    public static void main(String[] args) {\n        // Type your code here.\n    }\n}";
    }

    public static CompletableFuture<String> getChatGPTResponse(String message) {
        return CompletableFuture.supplyAsync(() -> {
            String prompt = "User: " + message + "\nChatGPT:";
            JSONObject requestData = new JSONObject()
                    .put("model", MODEL)
                    .put("prompt", prompt)
                    .put("max_tokens", Integer.parseInt(MAX_TOKENS))
                    .put("n", Integer.parseInt(N))
                    .put("stop", "\n");
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(ENDPOINT))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + API_KEY)
                        .POST(HttpRequest.BodyPublishers.ofString(requestData.toString()))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonObject responseJson = JsonParser.parseString(response.body()).getAsJsonObject();
                JsonArray choices = responseJson.getAsJsonArray("choices");
                LOGGER.info("Received response from ChatGPT API" + response);

                if (choices == null || choices.size() == 0) {
                    return "Failed to get a response from OpenAI API.";
                }
                JsonArray choices2 = responseJson.getAsJsonArray("choices");
                JsonObject choice = choices2.get(0).getAsJsonObject();
                return choice.get("text").getAsString().replaceAll("\"", "").replaceAll("'", "\"").trim();
            } catch (Exception e) {
                e.printStackTrace();
                return "An error occurred while processing the request.";
            }
        });
    }

    public static Toml readOrCreateConfig() throws IOException {
        Path configFile = Paths.get(Opotato.CHAT_GPT_CONFIG);
        if (!Files.exists(configFile)) {
            Files.createDirectories(configFile.getParent());
            Files.createFile(configFile);
            Toml toml = new Toml();
            TomlWriter writer = new TomlWriter();
            writer.write(toml, new File(Opotato.CHAT_GPT_CONFIG));
        }
        return new Toml().read(new File(Opotato.CHAT_GPT_CONFIG));
    }
    public static boolean isValidApiKey(String apiKey) {
        return apiKey.startsWith("sk-");
    }
}

package com.teampotato.opotato.util.chatgpt;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.moandjiezana.toml.Toml;
import com.teampotato.opotato.Opotato;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import static com.teampotato.opotato.util.chatgpt.TomlUtils.MODEL;

public class ChatGPTUtils {
    public static String generatePrompt(String message) {
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        return "System.out.println(\"" + encodedMessage + "\");\n\npublic class MyFirstProgram {\n    public static void main(String[] args) {\n\n    }\n}";
    }

    public static CompletableFuture<String> getChatGPTResponse(String message) {
        return CompletableFuture.supplyAsync(() -> {
            String prompt = "User: " + message + "\nChatGPT:";
            JSONObject requestData = new JSONObject()
                    .put("model", MODEL)
                    .put("prompt", prompt)
                    .put("max_tokens", Integer.parseInt(TomlUtils.MAX_TOKENS))
                    .put("n", Integer.parseInt(TomlUtils.N))
                    .put("stop", "\n");
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(TomlUtils.ENDPOINT))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + TomlUtils.API_KEY)
                        .POST(HttpRequest.BodyPublishers.ofString(requestData.toString()))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonObject responseJson = JsonParser.parseString(response.body()).getAsJsonObject();
                JsonArray choices = responseJson.getAsJsonArray("choices");
                Opotato.LOGGER.info("Received response from ChatGPT API" + response);

                if (choices == null || choices.size() == 0) return "Failed to get a response from OpenAI API.";
                JsonObject choice = choices.get(0).getAsJsonObject();
                return choice.get("text").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
                return "An error occurred while processing the request.";
            }
        });
    }

    public static void createConfig() {
        File file = new File(Opotato.CHATGPT_CONFIG);
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write("endpoint = \"API_Link\"\n");
                writer.write("api_key = \"OpenAI_API_Key\"\n");
                writer.write("model = \"AI_Model\"\n");
                writer.write("prompt = \"Undeveloped\"\n");
                writer.write("max_tokens = \"Max_Tokens\"\n");
                writer.write("n = \"N\"\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void initChatGPTInfo() {
        Toml toml = TomlUtils.readTomlFromFile(Opotato.CHATGPT_CONFIG);
        TomlUtils.ENDPOINT = toml.getString("endpoint");
        TomlUtils.API_KEY = toml.getString("api_key");
        MODEL = toml.getString("model");
        TomlUtils.MAX_TOKENS = toml.getString("max_tokens");
        TomlUtils.N = toml.getString("n");
    }
}

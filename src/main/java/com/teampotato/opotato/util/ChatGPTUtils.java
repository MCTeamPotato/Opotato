package com.teampotato.opotato.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teampotato.opotato.config.PotatoCommonConfig;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class ChatGPTUtils {
    public static String generatePrompt(String message) {
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        return "System.out.println(\"" + encodedMessage + "\");\n\npublic class MyFirstProgram {\n    public static void main(String[] args) {\n\n    }\n}";
    }

    public static CompletableFuture<String> getChatGPTResponse(String message) {
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

}

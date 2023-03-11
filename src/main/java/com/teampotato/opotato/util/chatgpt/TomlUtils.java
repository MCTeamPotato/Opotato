package com.teampotato.opotato.util.chatgpt;

import com.moandjiezana.toml.Toml;
import net.fabricmc.api.ModInitializer;

import java.io.FileReader;
import java.io.IOException;

public class TomlUtils implements ModInitializer {
    public static String ENDPOINT;
    public static String API_KEY;
    public static String MODEL;
    public static String MAX_TOKENS;
    public static String N;

    public static void loadConfig() {
        Toml toml = readTomlFromFile("config/chatgpt.toml");

        ENDPOINT = toml.getString("endpoint");
        API_KEY = toml.getString("api_key");
        MODEL = toml.getString("model");
        MAX_TOKENS = toml.getString("max_tokens");
        N = toml.getString("n");
        System.out.println("-----------------------------------------");
        System.out.println("[Potato-ChatGPT]Your ChatGPT Config Info:");
        System.out.println("-----------------------------------------");
        System.out.println("Endpoint: " + ENDPOINT);
        System.out.println("API key: " + API_KEY);
        System.out.println("Model: " + MODEL);
        System.out.println("Max tokens: " + MAX_TOKENS);
        System.out.println("N: " + N);
        System.out.println("-----------------------------------------");
    }

    public static Toml readTomlFromFile(String path) {
        Toml toml = new Toml();
        try {
            toml = new Toml().read(new FileReader(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toml;
    }

    @Override
    public void onInitialize() {
        loadConfig();
    }
}


package com.teampotato.opotato.util.chatgpt;

import com.moandjiezana.toml.Toml;
import com.teampotato.opotato.Opotato;

import java.io.FileReader;
import java.io.IOException;

public class TomlUtils {
    public static String ENDPOINT;
    public static String API_KEY;
    public static String MODEL;
    public static String MAX_TOKENS;
    public static String N;

    public static void loadConfig() {
        Toml toml = readTomlFromFile("config/Opotato_MineGPTConfig.toml");

        ENDPOINT = toml.getString("endpoint");
        API_KEY = toml.getString("api_key");
        MODEL = toml.getString("model");
        MAX_TOKENS = toml.getString("max_tokens");
        N = toml.getString("n");
        Opotato.LOGGER.info("-----------------------------------------");
        Opotato.LOGGER.info("[Opotato-MineGPT]Your ChatGPT Config Info:");
        Opotato.LOGGER.info("-----------------------------------------");
        Opotato.LOGGER.info("Endpoint: {}", ENDPOINT);
        Opotato.LOGGER.info("Model: {}" , MODEL);
        Opotato.LOGGER.info("Max tokens: {}" , MAX_TOKENS);
        Opotato.LOGGER.info("N: {}" , N);
        Opotato.LOGGER.info("-----------------------------------------");
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

    public static void reloadConfig() {
        Toml toml = readTomlFromFile("config/Opotato_MineGPTConfig.toml");
        ENDPOINT = toml.getString("endpoint");
        API_KEY = toml.getString("api_key");
        MODEL = toml.getString("model");
        MAX_TOKENS = toml.getString("max_tokens");
        N = toml.getString("n");
    }
}

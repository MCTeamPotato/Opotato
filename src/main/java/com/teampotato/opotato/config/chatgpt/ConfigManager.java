package com.teampotato.opotato.config.chatgpt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.teampotato.opotato.Opotato;
import me.shedaniel.architectury.platform.Platform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path configPath = Platform.getConfigFolder().resolve(Opotato.ID + "_chatgpt.json");

    public static void loadConfig() {
        Config config = load();

        if (config.token == null || config.secret == null) {
            config.token = "";
            config.secret = "";
            Opotato.LOGGER.info("Token or secret not found, resetting");
        }

        Config.update(config);
        save();
        Opotato.LOGGER.info("Loaded config");
    }

    private static Config load() {
        Config config = Config.getInstance();
        try {
            if (!Files.exists(configPath)) {
                Files.createDirectories(configPath.getParent());
                Files.createFile(configPath);
                return config;
            }
            try {
                config = GSON.fromJson(Files.newBufferedReader(configPath), Config.class);
            } catch (JsonSyntaxException e) {
                Opotato.LOGGER.error("Failed to parse config file, using default config");
                config = Config.getInstance();
            }
        } catch (IOException e) {
            Opotato.LOGGER.error("Failed to load config", e);
        }
        return config == null ? Config.getInstance() : config;
    }

    private static void save() {
        try {
            Files.write(configPath, GSON.toJson(Config.getInstance()).getBytes());
        } catch (IOException e) {
            Opotato.LOGGER.error("Failed to save config", e);
        }
    }

    public static void saveConfig() {
        save();
        Opotato.LOGGER.info("Saved config");
    }
}

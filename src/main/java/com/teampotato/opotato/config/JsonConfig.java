package com.teampotato.opotato.config;

import com.google.gson.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JsonConfig {
    private static final JsonParser PARSER = new JsonParser();

    private final Path configPath;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, JsonElement> configMap = new Object2ObjectOpenHashMap<>();

    public static final Path CONFIG_DIR = FMLLoader.getGamePath().resolve("config");

    private JsonConfig(@NotNull Path configPath, String version) {
        this.configPath = configPath;
        this.put("Version", version);
    }

    @Contract("_, _ -> new")
    public static @NotNull JsonConfig create(Path configPath, String version) {
        return new JsonConfig(configPath, version);
    }

    @Contract("_, _ -> new")
    public static @NotNull JsonConfig create(String modID, String version) {
        return create(CONFIG_DIR.resolve(modID + ".json"), version);
    }

    public JsonConfig initialize() {
        if (Files.exists(this.configPath)) {
            read();
        } else {
            create();
        }
        return this;
    }

    private void read() {
        try (BufferedReader reader = new BufferedReader(new FileReader(configPath.toFile()))) {
            JsonObject fileConfig = PARSER.parse(reader).getAsJsonObject();

            Map<String, JsonElement> defaultConfig = new Object2ObjectOpenHashMap<>(this.configMap);

            this.configMap.clear();
            for (Map.Entry<String, JsonElement> entry : fileConfig.entrySet()) {
                this.configMap.put(entry.getKey(), entry.getValue());
            }

            for (Map.Entry<String, JsonElement> entry : defaultConfig.entrySet()) {
                if (!this.configMap.containsKey(entry.getKey())) {
                    this.configMap.put(entry.getKey(), entry.getValue());
                }
            }

            JsonElement fileVersion = fileConfig.get("Version");
            if (fileVersion == null || !fileVersion.getAsString().equals(defaultConfig.get("Version").getAsString())) {
                this.configMap.put("Version", defaultConfig.get("Version"));
                update();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file: " + configPath, e);
        }
    }

    private void create() {
        try {
            Files.createDirectories(this.configPath.getParent());
            saveToFile();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create config file: " + configPath, e);
        }
    }

    private void update() {
        saveToFile();
    }

    private void saveToFile() {
        try (Writer writer = new FileWriter(configPath.toFile())) {
            gson.toJson(configMap, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save config file: " + configPath, e);
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public JsonConfig put(String key, Object value) {
        this.configMap.put(key, gson.toJsonTree(value));
        return this;
    }

    private JsonElement get(String key) {
        return this.configMap.get(key);
    }

    public int getInt(String key) {
        return get(key).getAsInt();
    }

    public double getDouble(String key) {
        return get(key).getAsDouble();
    }

    public float getFloat(String key) {
        return get(key).getAsFloat();
    }

    public long getLong(String key) {
        return get(key).getAsLong();
    }

    public boolean getBoolean(String key) {
        return get(key).getAsBoolean();
    }

    public String getString(String key) {
        return get(key).getAsString();
    }

    public <T> Stream<T> getStream(String key, @NotNull Class<T> valueType) {
        return StreamSupport.stream(this.configMap.get(key).getAsJsonArray().spliterator(), false).map(element -> gson.fromJson(element, valueType));
    }

    public <T> List<T> getList(String key, @NotNull Class<T> valueType) {
        return getStream(key, valueType).collect(Collectors.toList());
    }

    public <T> Set<T> getSet(String key, @NotNull Class<T> valueType) {
        return getStream(key, valueType).collect(Collectors.toSet());
    }
}
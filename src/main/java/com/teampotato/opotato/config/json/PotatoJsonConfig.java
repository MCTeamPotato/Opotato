package com.teampotato.opotato.config.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teampotato.opotato.EarlySetupInitializer;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class PotatoJsonConfig {
    public boolean printModListWhenLaunching;
    public boolean showModCompatibilityWarning;

    public boolean enableCreativeOnePouch;

    public static boolean initFailed;

    @Nullable public static Exception writeException;
    @Nullable public static Exception readException;

    public PotatoJsonConfig() {
        File configDir = new File(FMLLoader.getGamePath().resolve("config").toFile(), "opotato");
        configDir.mkdirs();
        File configFile = new File(configDir, EarlySetupInitializer.MOD_ID + "-common.json");
        if (!configFile.exists()) {
            try {
                FileWriter writer = writeFile(configFile);
                writer.close();
            } catch (Exception e) {
                initFailed = true;
                writeException = e;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            JsonObject config = new JsonParser().parse(reader).getAsJsonObject();
            printModListWhenLaunching = config.get("printModListWhenLaunching").getAsBoolean();
            showModCompatibilityWarning = config.get("showModCompatibilityWarning").getAsBoolean();
            showModCompatibilityWarning = config.get("enableCreativeOnePouch").getAsBoolean();
        } catch (Exception e) {
            initFailed = true;
            readException = e;
        }
    }

    @NotNull
    private static FileWriter writeFile(File configFile) throws IOException {
        JsonObject defaultConfig = new JsonObject();
        defaultConfig.addProperty("printModListWhenLaunching", true);
        defaultConfig.addProperty("showModCompatibilityWarning", true);
        defaultConfig.addProperty("enableCreativeOnePouch", false);
        FileWriter writer = new FileWriter(configFile);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(defaultConfig, writer);
        return writer;
    }
}

package com.teampotato.opotato.config.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.mixin.EarlySetupInitializer;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class PotatoJsonConfig {
    public boolean printModListWhenLaunching;

    public static boolean initFailed;

    public PotatoJsonConfig() {
        EarlySetupInitializer.LOGGER.warn("Why does the json config file exist?");
        EarlySetupInitializer.LOGGER.warn("Because Forge Config API sucks, which cannot load into the game as soon as the game get launched.");
        File configDir = new File(FMLLoader.getGamePath().toFile(), "config");
        configDir.mkdirs();
        File configFile = new File(configDir, Opotato.MOD_ID + ".json");
        if (!configFile.exists()) {
            try {
                FileWriter writer = writeFile(configFile);
                writer.close();
            } catch (Exception e) {
                initFailed = true;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            JsonObject config = new JsonParser().parse(reader).getAsJsonObject();
            printModListWhenLaunching = config.get("printModListWhenLaunching").getAsBoolean();
        } catch (Exception e) {
            initFailed = true;
        }
    }

    @NotNull
    private static FileWriter writeFile(File configFile) throws IOException {
        JsonObject defaultConfig = new JsonObject();
        defaultConfig.addProperty("printModListWhenLaunching", true);
        FileWriter writer = new FileWriter(configFile);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(defaultConfig, writer);
        return writer;
    }
}

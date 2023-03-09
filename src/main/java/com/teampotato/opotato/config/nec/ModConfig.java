package com.teampotato.opotato.config.nec;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.platform.NecPlatform;

import java.io.*;

public class ModConfig {

    public enum CrashLogUploadType {
        GIST
    }

    private static final File CONFIG_FILE = new File(NecPlatform.instance().getConfigDirectory().toFile(), Opotato.ID + "_neconfig.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ModConfig instance = null;

    public CrashLogUploadType uploadCrashLogTo = CrashLogUploadType.GIST;
    public boolean disableReturnToMainMenu = false;
    public boolean deobfuscateStackTrace = true;
    public boolean debugModIdentification = false;
    public boolean forceCrashScreen = false;

    public static ModConfig instance() {
        if (instance != null) {
            return instance;
        }

        if (CONFIG_FILE.exists()) {
            try {
                return instance = new Gson().fromJson(new FileReader(CONFIG_FILE), ModConfig.class);
            } catch (FileNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }

        instance = new ModConfig();

        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(instance, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return instance;
    }
}

package com.teampotato.opotato;

import com.moandjiezana.toml.Toml;
import com.teampotato.opotato.config.PotatoCommonConfig;
import com.teampotato.opotato.util.alternatecurrent.profiler.ACProfiler;
import com.teampotato.opotato.util.alternatecurrent.profiler.Profiler;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Mod(Opotato.ID)
public class Opotato {

    public static String ENDPOINT;
    public static String API_KEY;
    public static String MODEL;
    public static String MAX_TOKENS;
    public static String N;

    public static final String ID = "opotato";
    public static final String NAME = "Opotato";
    public static final String VER = "1.4.0";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static List<LevelChunk> loadedChunks = new ArrayList<>();

    public static boolean on = true;

    public static Profiler creatrProfiler() {
        return PotatoCommonConfig.ALTERNATE_CURRENT_DEBUG_MODE.get() ? new ACProfiler() : Profiler.DUMMY;
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

    public Opotato() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PotatoCommonConfig.COMMON_CONFIG);


        File file = new File("config/chatgpt.toml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write("endpoint = \"YOUR_ENDPOINT\"\n");
                writer.write("api_key = \"YOUR_OPENAI_APIKEY\"\n");
                writer.write("model = \"YOUR_OPENAI_MODEL\"\n");
                writer.write("prompt = \"YOUR_PROMPT\"\n");
                writer.write("max_tokens = \"YOUR_MAX_TOKENS\"\n");
                writer.write("n = \"YOUR_N\"\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Toml toml = readTomlFromFile("config/chatgpt.toml");

        ENDPOINT = toml.getString("endpoint");
        API_KEY = toml.getString("api_key");
        MODEL = toml.getString("model");
        MAX_TOKENS = toml.getString("max_tokens");
        N = toml.getString("n");
    }
}

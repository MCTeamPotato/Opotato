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
    }
}

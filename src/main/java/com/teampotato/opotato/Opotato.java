package com.teampotato.opotato;

import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(Opotato.ID)
public class Opotato {

    public static final String ID = "opotato";
    public static final Logger LOGGER = LogManager.getLogger(ID);
    public static List<Chunk> loadedChunks = new ArrayList<>();

    public Opotato() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PotatoCommonConfig.COMMON_CONFIG);
    }
}

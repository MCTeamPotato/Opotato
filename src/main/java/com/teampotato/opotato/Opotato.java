package com.teampotato.opotato;

import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Opotato.ID)
public class Opotato {
    public static final String ID = "opotato";
    public static final Logger LOGGER = LogManager.getLogger(ID);
    public Opotato() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PotatoCommonConfig.COMMON_CONFIG);
        if (PotatoCommonConfig.PRINT_MOD_LIST_WHEN_LAUNCHING_GAME.get()) ModList.get().getMods().forEach(modInfo -> LOGGER.info("Mod " + modInfo.getOwningFile().getFile().getFileName() + " loaded!"));
        LOGGER.info("Oh, potato!");
    }
}

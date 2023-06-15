package com.teampotato.opotato;

import com.teampotato.opotato.config.PotatoCommonConfig;
import com.teampotato.opotato.event.OstEvents;
import com.teampotato.opotato.event.WitherStormCacheEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.ModLoadingWarning;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CopyOnWriteArrayList;

@Mod(Opotato.ID)
public class Opotato {
    public static final String ID = "opotato";
    public static final Logger LOGGER = LogManager.getLogger(ID);
    public static CopyOnWriteArrayList<Chunk> loadedChunks = new CopyOnWriteArrayList<>();

    public Opotato() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PotatoCommonConfig.COMMON_CONFIG);
        FMLLoader.getLoadingModList().getModFiles().forEach(modInfo -> LOGGER.info("Opotato: Mod " + modInfo.getFile().getFileName() + " loaded!"));
        LOGGER.info("Oh, potato!");
        if (isLoaded("ostoverhaul")) MinecraftForge.EVENT_BUS.register(OstEvents.class);
        if (isLoaded("witherstormmod")) MinecraftForge.EVENT_BUS.register(WitherStormCacheEvent.class);
    }

    public static class EmptyThread extends Thread {
        public EmptyThread() {
            this.setName("Empty Thread");
            this.setDaemon(true);
            this.start();
        }
        public void run() {}
    }

    public static boolean isLoaded(String modID) {
        return FMLLoader.getLoadingModList().getModFileById(modID) != null;
    }

    public static void exeCmd(MinecraftServer server, String cmd) {
        server.getCommands().performCommand(server.createCommandSourceStack().withSuppressedOutput(), cmd);
    }

    public static void addIncompatibleWarn(FMLCommonSetupEvent event, String translationKey) {
        event.enqueueWork(() -> ModLoader.get().addWarning(new ModLoadingWarning(ModLoadingContext.get().getActiveContainer().getModInfo(), ModLoadingStage.COMMON_SETUP, translationKey)));
    }
}
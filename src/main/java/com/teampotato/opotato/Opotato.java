package com.teampotato.opotato;

import com.teampotato.opotato.command.OpotatoCommand;
import com.teampotato.opotato.config.PotatoCommonConfig;
import com.teampotato.opotato.config.nec.NecConfig;
import com.teampotato.opotato.platform.CommonModMetadata;
import com.teampotato.opotato.platform.NecPlatform;
import com.teampotato.opotato.util.profiler.ACProfiler;
import com.teampotato.opotato.util.profiler.Profiler;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Mod(Opotato.ID)
public class Opotato {
    public Opotato() {
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PotatoCommonConfig.COMMON_CONFIG);
        if (PotatoCommonConfig.ALTERNATE_CURRENT_DEBUG_MODE.get()) LOGGER.info("Opotato - Alternate Current DEBUG mode enabled!");
        EventBuses.registerModEventBus(Opotato.ID, FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final String ID = "opotato";
    public static final String NAME = "Opotato";
    public static final String VER = "1.2.0";
    public static final Logger LOGGER = LogManager.getLogger(ID);
    public static final Path DIRECTORY = NecPlatform.instance().getGameDirectory().resolve("nec");

    public static List<LevelChunk> loadedChunks = new ArrayList<>();

    public static boolean on = true;

    public static Profiler creatrProfiler() {
        return PotatoCommonConfig.ALTERNATE_CURRENT_DEBUG_MODE.get() ? new ACProfiler() : Profiler.DUMMY;
    }

    public static void ensureDirectoryExists() throws IOException {
        Files.createDirectories(DIRECTORY);
    }

    public static void initialize() {
        NecConfig.instance();
    }

    public static boolean enableEntrypointCatching() {
        return NecConfig.instance().catchInitializationCrashes;
    }

    public static final boolean ENABLE_GAMELOOP_CATCHING = true;

    public static CommonModMetadata getMetadata() {
        List<CommonModMetadata> mods = NecPlatform.instance().getModMetadatas(ID);
        if (mods.size() != 1) throw new IllegalStateException("NEC should have exactly one mod under its ID");
        return mods.get(0);
    }

    @Mod.EventBusSubscriber
    public static class ModEvents {
        @SubscribeEvent
        public static void onRegisterCommands(RegisterCommandsEvent event) {
            OpotatoCommand.register(event.getDispatcher());
        }
    }
}

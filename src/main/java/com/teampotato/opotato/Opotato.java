package com.teampotato.opotato;

import com.teampotato.opotato.config.PotatoCommonConfig;
import com.teampotato.opotato.util.profiler.ACProfiler;
import com.teampotato.opotato.util.profiler.Profiler;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(Opotato.ID)
public class Opotato {
    public Opotato() {
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PotatoCommonConfig.COMMON_CONFIG);
        if (PotatoCommonConfig.ALTERNATE_CURRENT_DEBUG_MODE.get()) LOGGER.info("Opotato - Alternate Current DEBUG mode enabled!");
    }

    public static final String ID = "opotato";
    public static final String NAME = "Opotato";
    public static final String VER = "1.0.0";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static List<LevelChunk> loadedChunks = new ArrayList<>();

    public static boolean on = true;

    public static Profiler creatrProfiler() {
        return PotatoCommonConfig.ALTERNATE_CURRENT_DEBUG_MODE.get() ? new ACProfiler() : Profiler.DUMMY;
    }

    @Mod.EventBusSubscriber(modid = ID)
    public static class ModEvents {
        @SubscribeEvent
        public static void onRegisterCommands(RegisterCommandsEvent event) {
            OpotatoCommand.register(event.getDispatcher());
        }
    }
}

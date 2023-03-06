package com.teampotato.potatoptimize;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.teampotato.potatoptimize.profiler.ACProfiler;
import com.teampotato.potatoptimize.profiler.Profiler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.teampotato.potatoptimize.Config.COMMON_CONFIG;

@Mod(PotatOptimize.ID)
public class PotatOptimize {
    public PotatOptimize() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static final String ID = "potatoptimize";
    public static final String NAME = "PotatOptimize";
    public static final String VER = "1.0";
    public static final Logger LOGGER = LogManager.getLogger(ID);
    public static final boolean DEBUG = false;

    public static boolean on = true;

    public static Profiler creatrProfiler() {
        return DEBUG ? new ACProfiler() : Profiler.DUMMY;
    }

    @Mod.EventBusSubscriber(modid = ID)
    public static class ModEvents {
        @SubscribeEvent
        public static void onRegisterCommands(RegisterCommandsEvent event) {
            PotatOptimizeCommand.register(event.getDispatcher());
        }
    }
}

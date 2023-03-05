package com.teampotato.potatoptimize;

import com.teampotato.potatoptimize.profiler.ACProfiler;
import com.teampotato.potatoptimize.profiler.Profiler;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(PotatOptimize.ID)
public class PotatOptimize {
    public PotatOptimize() {
        System.out.println("Yea another optimization mod.");
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

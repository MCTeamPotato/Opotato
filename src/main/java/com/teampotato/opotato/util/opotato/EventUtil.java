package com.teampotato.opotato.util.opotato;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLLoader;

public class EventUtil {
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

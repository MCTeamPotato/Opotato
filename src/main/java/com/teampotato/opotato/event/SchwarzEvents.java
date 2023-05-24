package com.teampotato.opotato.event;

import com.teampotato.opotato.util.schwarz.command.Command;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SchwarzEvents {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        Command.register(event.getDispatcher());
    }
}

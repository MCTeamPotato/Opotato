package com.teampotato.opotato.command.chatgpt;

import me.shedaniel.architectury.event.events.CommandRegistrationEvent;

public abstract class Commands {
    public static void init() {
        CommandRegistrationEvent.EVENT.register((dispatcher, registryAccess) -> {
            AuthCommand.register(dispatcher);
            AskCommand.register(dispatcher);
            PreviousConversationCommand.register(dispatcher);
            NewConversationCommand.register(dispatcher);
        });
    }
}

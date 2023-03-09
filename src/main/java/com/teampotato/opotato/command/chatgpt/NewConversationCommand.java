package com.teampotato.opotato.command.chatgpt;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.util.ChatGPTUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class NewConversationCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("newconversation").executes(context -> execute(context.getSource()));
        dispatcher.register(builder);
    }

    private static int execute(CommandSourceStack source) {
        boolean newConversation = ChatGPTUtils.nextConversation();
        int index = ChatGPTUtils.getConversationIndex();
        if(newConversation) {
            source.sendSuccess(Component.nullToEmpty("§b[MCGPT]: §fStarted a new conversation (" + (index + 1) + ")"), false);
        } else {
            source.sendSuccess(Component.nullToEmpty("§b[MCGPT]: §fContinuing conversation (" + (index + 1) + ")"), false);
        }
        return 1;
    }
}

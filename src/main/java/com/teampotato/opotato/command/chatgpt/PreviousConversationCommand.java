package com.teampotato.opotato.command.chatgpt;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.teampotato.opotato.util.ChatGPTUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class PreviousConversationCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("previousconversation").executes(context -> execute(context.getSource()));
        dispatcher.register(builder);
    }

    private static int execute(CommandSourceStack source) {
        ChatGPTUtils.previousConversation();
        int index = ChatGPTUtils.getConversationIndex();
        source.sendSuccess(Component.nullToEmpty("§b[MCGPT]: §fContinuing conversation (" + (index + 1) + ")"), false);
        return 1;
    }
}

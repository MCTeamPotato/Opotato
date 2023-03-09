package com.teampotato.opotato.command.chatgpt;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.teampotato.opotato.util.ChatGPTUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class AskCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("ask")
                .then(Commands.argument("question", StringArgumentType.greedyString()).executes(context -> execute(context.getSource(), StringArgumentType.getString(context, "question"))));
        dispatcher.register(builder);
    }

    private static int execute(CommandSourceStack source, String question) throws CommandSyntaxException {
        source.sendSuccess(Component.nullToEmpty("§7<" + source.getPlayerOrException().getDisplayName().getString() + "> " + question), false);
        ChatGPTUtils.ask(question);
        return 1;
    }
}
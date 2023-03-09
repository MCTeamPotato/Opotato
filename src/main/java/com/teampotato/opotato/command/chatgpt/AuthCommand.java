package com.teampotato.opotato.command.chatgpt;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.config.chatgpt.Config;
import com.teampotato.opotato.config.chatgpt.ConfigManager;
import com.teampotato.opotato.store.SecureTokenStorage;
import com.teampotato.opotato.util.ChatGPTUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class AuthCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("mcpgt-auth")
                .then(Commands.argument("token", StringArgumentType.string()).executes(context -> execute(context.getSource(), StringArgumentType.getString(context, "token"))));
        dispatcher.register(builder);
    }

    private static int execute(CommandSourceStack source, String token) {
        if(token.length() != 51) {
            Opotato.LOGGER.error("Invalid token length");
            source.sendSuccess(Component.nullToEmpty("§b[MCGPT]: §cInvalid token"), false);
            return 0;
        }
        if(!token.startsWith("sk-")) {
            Opotato.LOGGER.error("Invalid token prefix");
            source.sendSuccess(Component.nullToEmpty("§b[MCGPT]: §cInvalid token"), false);
            return 0;
        }
        Config.getInstance().token = SecureTokenStorage.encrypt(token);
        ConfigManager.saveConfig();
        ChatGPTUtils.startService();
        source.sendSuccess(Component.nullToEmpty("§b[MCGPT]: §aSuccessfully authenticated"), false);
        return 1;
    }
}

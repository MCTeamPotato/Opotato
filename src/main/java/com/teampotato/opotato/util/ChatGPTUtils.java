package com.teampotato.opotato.util;

import com.teampotato.opotato.command.chatgpt.Commands;
import com.teampotato.opotato.config.chatgpt.Config;
import com.teampotato.opotato.config.chatgpt.ConfigManager;
import com.teampotato.opotato.store.SecureTokenStorage;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import me.shedaniel.architectury.event.events.client.ClientPlayerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatGPTUtils {
    private static final ExecutorService executor;

    private static OpenAiService service;
    private static List<List<ChatMessage>> conversations;
    private static int conversationIndex = 0;

    static {
        executor = Executors.newFixedThreadPool(1);
    }

    public static void init() {
        conversations = new ArrayList<>();

        Commands.init();
        ConfigManager.loadConfig();

        if(!Config.getInstance().token.isEmpty()) {
            startService();
        }

        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(player -> {
            if(!notAuthed(false)) {
                player.sendMessage(Component.nullToEmpty("§b[MCGPT]: §aSuccessfully authenticated"), player.getUUID());
            }
        });
    }

    public static void startService() {
        service = new OpenAiService(SecureTokenStorage.decrypt(Config.getInstance().secret, Config.getInstance().token));
    }

    public static boolean notAuthed() {
        return notAuthed(true);
    }

    public static boolean notAuthed(boolean prompt) {
        if(service == null) {
            LocalPlayer player = Minecraft.getInstance().player;
            if(player != null && prompt) {
                player.sendMessage(Component.nullToEmpty("§cPlease authenticate with an OpenAI token using /mcgpt-auth <token>"), player.getUUID());
                player.sendMessage(Component.nullToEmpty("§cIf you do not have a token, you can generate one here:"), player.getUUID());
                player.sendMessage(Component.nullToEmpty("§chttps://platform.openai.com/account/api-keys").copy().withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://platform.openai.com/account/api-keys"))), player.getUUID());
            }
            return true;
        }
        return false;
    }

    public static int getConversationIndex() {
        return conversationIndex;
    }

    public static boolean nextConversation() {
        if(notAuthed()) return false;
        if(conversationIndex < conversations.size() - 1) {
            conversationIndex++;
            return false;
        }
        conversations.add(new ArrayList<>());
        conversationIndex = conversations.size() - 1;
        conversations.get(conversationIndex).add(new ChatMessage("system", "You are an AI assistant in the game Minecraft. You are using the in game chat to communicate, thus, your responses should be quite short (256 characters max). Assume the player cannot access commands unless they explicitly ask for them."));
        return true;
    }

    public static void previousConversation() {
        if(notAuthed()) return;
        if(conversationIndex > 0) {
            conversationIndex--;
        }
    }

    private static void askSync(String question) {
        if(conversations.size() == 0) {
            nextConversation();
        }
        List<ChatMessage> conversation = conversations.get(conversationIndex);
        conversation.add(new ChatMessage("user", question));
        ChatCompletionRequest req = ChatCompletionRequest.builder()
                .messages(conversation)
                .model("gpt-3.5-turbo")
                .build();
        ChatMessage reply;
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return;
        try {
            reply = service.createChatCompletion(req).getChoices().get(0).getMessage();
            conversation.add(reply);
            if(conversation.size() > 10) {
                conversation.remove(1); // don't remove the first message, as it's the minecraft context
            }
            player.sendMessage(Component.nullToEmpty("<ChatGPT> " + reply.getContent().replaceAll("^\\s+|\\s+$", "")), player.getUUID());
        } catch (RuntimeException e) {
            player.sendMessage(Component.nullToEmpty("§b[MCGPT]: §cAn error occurred while communicating with OpenAI. Please check your internet connection, or try again later.").plainCopy().withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.nullToEmpty(e.getMessage())))), player.getUUID());
        }
    }

    public static void ask(String question) {
        if(notAuthed()) return;
        executor.execute(() -> {
            try {
                askSync(question);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
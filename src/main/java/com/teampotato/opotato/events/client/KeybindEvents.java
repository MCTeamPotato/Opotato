package com.teampotato.opotato.events.client;

import com.teampotato.opotato.config.PotatoCommonConfig;
import com.teampotato.opotato.events.CreativeOnePunch;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class KeybindEvents {
    public static final KeyMapping switchOnePunchKey = new KeyMapping("opotato.key.one_punch", GLFW.GLFW_KEY_UNKNOWN, "opotato.key.category");

    public static void onClientTick(TickEvent.ClientTickEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        if (switchOnePunchKey.consumeClick()) {
            CreativeOnePunch.creativeOnePunch = !CreativeOnePunch.creativeOnePunch;
            player.displayClientMessage(new TextComponent(I18n.get("opotato.creativeOnePunch") + (CreativeOnePunch.creativeOnePunch ? I18n.get("opotato.creativeOnePunch.true") : I18n.get("opotato.creativeOnePunch.false"))), true);
        }
    }

    public static void onClientSetup(@NotNull FMLClientSetupEvent event) {
        CreativeOnePunch.creativeOnePunch = PotatoCommonConfig.enableCreativeOnePouch.get();
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(switchOnePunchKey));
    }
}

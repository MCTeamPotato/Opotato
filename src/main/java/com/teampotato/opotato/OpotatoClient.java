package com.teampotato.opotato;

import com.teampotato.opotato.api.entity.NeatConfigChecker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class OpotatoClient {

    public static final KeyMapping SWITCH_ONE_PUNCH_KEY = new KeyMapping("opotato.key.one_punch", GLFW.GLFW_KEY_UNKNOWN, "opotato.key.category");

    public static void initClientEvents(@NotNull IEventBus forgeBus, @NotNull IEventBus modBus) {
        forgeBus.addListener(EventPriority.HIGHEST, (TickEvent.ClientTickEvent event) -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;
            if (OpotatoClient.SWITCH_ONE_PUNCH_KEY.consumeClick()) {
                EarlySetupInitializer.potatoJsonConfig.enableCreativeOnePouch = !EarlySetupInitializer.potatoJsonConfig.enableCreativeOnePouch;
                player.displayClientMessage(new TextComponent(I18n.get("opotato.creativeOnePunch") + (EarlySetupInitializer.potatoJsonConfig.enableCreativeOnePouch ? I18n.get("opotato.creativeOnePunch.true") : I18n.get("opotato.creativeOnePunch.false"))), true);
            }
        });

        modBus.addListener((FMLClientSetupEvent event) -> event.enqueueWork(() -> ClientRegistry.registerKeyBinding(SWITCH_ONE_PUNCH_KEY)));

        if (EarlySetupInitializer.isNeatLoaded) {
            modBus.addListener((FMLClientSetupEvent event) -> event.enqueueWork(() -> {
                for (EntityType<?> entityType : ForgeRegistries.ENTITIES) {
                    ResourceLocation id = entityType.getRegistryName();
                    if (id != null) ((NeatConfigChecker)entityType).potato$setIsInNeatBlacklist(vazkii.neat.NeatConfig.blacklist.contains(id.toString()));
                }
            }));
        }
    }
}

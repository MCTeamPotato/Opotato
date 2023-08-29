package com.teampotato.opotato.events.client;

import L_Ender.cataclysm.init.ModItems;
import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.config.PotatoCommonConfig;
import com.teampotato.opotato.events.CreativeOnePunch;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class KeybindEvents {
    public static class VoidCore {
        public static final KeyMapping voidCoreTriggerKey = new KeyMapping("opotato.key.cataclysm.voidCore", GLFW.GLFW_KEY_J, "opotato.key.cataclysm");

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> ClientRegistry.registerKeyBinding(voidCoreTriggerKey));
        }

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;
            if (voidCoreTriggerKey.consumeClick() && hasVoidCore(player)) player.addTag(Opotato.MOD_ID + ".voidCore");
        }

        private static boolean hasVoidCore(LocalPlayer player) {
            AtomicBoolean has = new AtomicBoolean(false);
            player.getCapability(CuriosCapability.INVENTORY).ifPresent(handler -> {
                Map<String, ICurioStacksHandler> curios = handler.getCurios();
                for (String id : curios.keySet()) {
                    ICurioStacksHandler stacksHandler = curios.get(id);
                    IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                    for (int i = 0; i < stackHandler.getSlots(); i++) {
                        ItemStack stack = stackHandler.getStackInSlot(i);

                        if (!stack.isEmpty() && stack.getItem().equals(ModItems.VOID_CORE.get())) {
                            has.set(true);
                            break;
                        }
                    }
                    if (has.get()) break;
                }
            });
            return has.get();
        }
    }

    public static class OnePunch {
        public static final KeyMapping switchOnePunchKey = new KeyMapping("opotato.key.one_punch", GLFW.GLFW_KEY_C, "opotato.key.category");

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;
            if (switchOnePunchKey.consumeClick()) {
                CreativeOnePunch.creativeOnePunch = !CreativeOnePunch.creativeOnePunch;
                player.displayClientMessage(new TextComponent(I18n.get("opotato.creativeOnePunch") + (CreativeOnePunch.creativeOnePunch ? I18n.get("opotato.creativeOnePunch.true") : I18n.get("opotato.creativeOnePunch.false"))), true);
            }
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            CreativeOnePunch.creativeOnePunch = PotatoCommonConfig.enableCreativeOnePouch.get();
            event.enqueueWork(() -> ClientRegistry.registerKeyBinding(switchOnePunchKey));
        }
    }
}

package com.teampotato.opotato.events;

import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.config.PotatoCommonConfig;
import com.teampotato.opotato.events.client.KeybindEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Opotato.MOD_ID)
public class CreativeOnePunch {
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Entity source = event.getSource().getDirectEntity();
        if (source instanceof ServerPlayer && PotatoCommonConfig.enableCreativeOnePouch.get() && KeybindEvents.creativeOnePunch) {
            ServerPlayer player = (ServerPlayer) source;
            if (player.isCreative()) {
                event.getEntityLiving().setHealth(0.0F);
            }
        }
    }
}

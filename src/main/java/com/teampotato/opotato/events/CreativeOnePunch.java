package com.teampotato.opotato.events;

import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CreativeOnePunch {
    public static boolean creativeOnePunch;

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Entity source = event.getSource().getDirectEntity();
        if (source instanceof ServerPlayer && PotatoCommonConfig.enableCreativeOnePouch.get() && creativeOnePunch) {
            ServerPlayer player = (ServerPlayer) source;
            if (player.isCreative()) event.getEntityLiving().setHealth(0.0F);
        }
    }
}

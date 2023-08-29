package com.teampotato.opotato.events;

import com.teampotato.opotato.config.WitherStormExtraConfig;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;

import java.util.UUID;

public class WitherStormEvents {
    public static UUID witherStormUUID = null;
    private static final String[] targets = new String[]{"block_cluster", "sickened_skeleton", "sickened_creeper", "sickened_spider", "sickened_zombie", "tentacle", "withered_symbiont"};


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        MinecraftServer server = entity.getServer();
        if (entity instanceof CommandBlockEntity && server != null && !event.isCanceled()) {
            witherStormUUID = null;
            if (WitherStormExtraConfig.killAllWitherStormModEntitiesWhenTheCommandBlockDies.get()) {
                for (String target : targets) server.getCommands().performCommand(server.createCommandSourceStack().withSuppressedOutput(), "/kill @e[type=witherstormmod:" + target + "]");
            }
        }
    }
}

package com.teampotato.opotato.events;

import com.teampotato.opotato.config.mods.WitherStormExtraConfig;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;
import org.jetbrains.annotations.NotNull;

public class WitherStormCleaner {
    private static final String[] targets = new String[]{"block_cluster", "sickened_skeleton", "sickened_creeper", "sickened_spider", "sickened_zombie", "tentacle", "withered_symbiont"};

    @SubscribeEvent
    public static void onLivingDeath(@NotNull LivingDeathEvent event) {
        if (!WitherStormExtraConfig.killAllWitherStormModEntitiesWhenTheCommandBlockDies.get() || event.isCanceled()) return;
        LivingEntity entity = event.getEntityLiving();
        if (entity instanceof CommandBlockEntity && entity.level instanceof ServerLevel) {
            MinecraftServer server = ((ServerLevel) entity.level).getServer();
            for (String target : targets) {
                server.getCommands().performCommand(
                        server.createCommandSourceStack().withSuppressedOutput(),
                        "/kill @e[type=witherstormmod:" + target + "]"
                );
            }
        }
    }
}

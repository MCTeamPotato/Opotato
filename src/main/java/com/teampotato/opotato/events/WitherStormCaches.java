package com.teampotato.opotato.events;

import com.teampotato.opotato.config.mods.WitherStormExtraConfig;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.util.IEntitySyncableData;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WitherStormCaches {
    private static final String[] targets = new String[]{"block_cluster", "sickened_skeleton", "sickened_creeper", "sickened_spider", "sickened_zombie", "tentacle", "withered_symbiont"};
    public static Map<UUID, ResourceKey<Level>> witherStorms = new Object2ObjectOpenHashMap<>();
    public static List<UUID> dataSyncableEntities = new ObjectArrayList<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityLeaveWorld(EntityLeaveWorldEvent event) {
        Entity entity = event.getEntity();
        MinecraftServer server = entity.getServer();
        if (server == null) return;
        if (entity instanceof IEntitySyncableData) {
            dataSyncableEntities.remove(entity.getUUID());
            if (entity instanceof WitherStormEntity) {
                witherStorms.remove(entity.getUUID());
                if (WitherStormExtraConfig.killAllWitherStormModEntitiesWhenTheCommandBlockDies.get()) {
                    for (String target : targets) server.getCommands().performCommand(server.createCommandSourceStack().withSuppressedOutput(), "/kill @e[type=witherstormmod:" + target + "]");
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.isCanceled()) return;
        Entity entity = event.getEntity();
        Level level = event.getWorld();
        if (level instanceof ServerLevel) {
            if (entity instanceof WitherStormEntity) witherStorms.put(entity.getUUID(), entity.level.dimension());
            if (entity instanceof IEntitySyncableData) dataSyncableEntities.add(entity.getUUID());
        }
    }
}

package com.teampotato.opotato.events;

import L_Ender.cataclysm.entity.effect.Flame_Strike_Entity;
import com.teampotato.opotato.config.mods.WitherStormExtraConfig;
import com.teampotato.opotato.mixin.EarlySetupInitializer;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.util.IEntitySyncableData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EntitiesCacheEvent {
    private static final String[] targets = new String[]{"block_cluster", "sickened_skeleton", "sickened_creeper", "sickened_spider", "sickened_zombie", "tentacle", "withered_symbiont"};

    public static Map<UUID, ResourceKey<Level>> witherStorms = new Object2ObjectOpenHashMap<>();
    public static List<UUID> dataSyncableEntities = new ObjectArrayList<>();
    public static List<UUID> flameStrikes = new ObjectArrayList<>();
    public static List<UUID> itemEntities = new ObjectArrayList<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityLeaveWorld(@NotNull EntityLeaveWorldEvent event) {
        Entity entity = event.getEntity();
        MinecraftServer server = entity.getServer();
        UUID uuid = entity.getUUID();
        if (server == null) return;
        if (entity instanceof ItemEntity) {
            itemEntities.remove(uuid);
            return;
        }
        if (EarlySetupInitializer.isWitherStormModLoaded) {
            if (entity instanceof IEntitySyncableData) {
                dataSyncableEntities.remove(uuid);
                if (entity instanceof WitherStormEntity) {
                    witherStorms.remove(uuid);
                    if (WitherStormExtraConfig.killAllWitherStormModEntitiesWhenTheCommandBlockDies.get()) {
                        for (String target : targets) server.getCommands().performCommand(server.createCommandSourceStack().withSuppressedOutput(), "/kill @e[type=witherstormmod:" + target + "]");
                    }
                }
                return;
            }
        }
        if (EarlySetupInitializer.isCataclysmLoaded) {
            if (entity instanceof Flame_Strike_Entity) flameStrikes.remove(uuid);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityJoinWorld(@NotNull EntityJoinWorldEvent event) {
        if (event.isCanceled()) return;
        Entity entity = event.getEntity();
        Level level = event.getWorld();
        UUID uuid = entity.getUUID();
        if (level instanceof ServerLevel) {
            if (entity instanceof ItemEntity) {
                itemEntities.add(uuid);
                return;
            }
            if (EarlySetupInitializer.isWitherStormModLoaded) {
                if (entity instanceof IEntitySyncableData) {
                    if (entity instanceof WitherStormEntity) witherStorms.put(uuid, entity.level.dimension());
                    dataSyncableEntities.add(uuid);
                    return;
                }
            }
            if (EarlySetupInitializer.isCataclysmLoaded) {
                if (entity instanceof Flame_Strike_Entity) flameStrikes.add(uuid);
            }
        }
    }
}

package com.teampotato.opotato.event;

import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.Arrays;
import java.util.UUID;

import static com.teampotato.opotato.util.opotato.EventUtil.*;

@Mod.EventBusSubscriber(modid = Opotato.ID)
public class CommonEvents {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity.level.isClientSide) return;
        Entity source = event.getSource().getDirectEntity();
        if (Float.isNaN(entity.getHealth())) entity.setHealth(0.0F);
        if (Float.isNaN(event.getAmount())) event.setCanceled(true);

        if (!(source instanceof ServerPlayer player) || !PotatoCommonConfig.ENABLE_CREATIVE_ONE_POUCH.get() || !player.isCreative()) return;
        event.getEntityLiving().setHealth(0.0F);
    }
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!(event.getWorld() instanceof ServerLevel world)) return;
        Entity entity = event.getEntity();
        if (entity instanceof ServerPlayer) return;
        Entity existing = world.getEntity(entity.getUUID());
        if (existing == null || existing == entity) return;
        UUID newUUID = Mth.createInsecureUUID();
        while (world.getEntity(newUUID) != null) newUUID = Mth.createInsecureUUID();
        entity.setUUID(newUUID);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        try {
            Class.forName("net.optifine.Config");
            addIncompatibleWarn(event, "opotato.optnotfine");
        } catch (ClassNotFoundException ignored) {}
        if (isLoaded("rubidium")) {
            if (isLoaded("chunkanimator"))addIncompatibleWarn(event, "opotato.incompatible.rubidium.chunkanimator");
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntityLiving();
        Level world = entity.level;
        MinecraftServer server = world.getServer();
        ResourceLocation name = entity.getType().getRegistryName();
        if (!PotatoCommonConfig.KILL_WITHER_STORM_MOD_ENTITIES_AFTER_COMMAND_BLOCK_DIES.get() || world.isClientSide || name == null || server == null || !name.toString().equals("witherstormmod:command_block")) return;
        Arrays.stream(new String[]{"block_cluster", "sickened_skeleton", "sickened_creeper", "sickened_spider", "sickened_zombie", "tentacle", "withered_symbiont"}).forEach(obj -> exeCmd(server, "/kill @e[type=witherstormmod:" + obj + "]"));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void ctrlSpawn(LivingSpawnEvent.CheckSpawn event) {
        LivingEntity entity = event.getEntityLiving();
        LevelAccessor world = event.getWorld();
        ResourceLocation regName = entity.getType().getRegistryName();
        if (!PotatoCommonConfig.ALLOW_LIMIT_MAX_SPAWN.get() || regName == null || world.isClientSide() || PotatoCommonConfig.BLACKLIST.get().contains(regName.toString())) return;
        ChunkPos chunk = world.getChunk(entity.blockPosition()).getPos();
        if (world.getEntitiesOfClass(entity.getClass(), new AABB(chunk.getMinBlockX(), 0, chunk.getMinBlockZ(), chunk.getMaxBlockX(), 256, chunk.getMaxBlockX())).size() > PotatoCommonConfig.MAX_ENTITIES_NUMBER_PER_CHUNK.get()) event.setResult(Event.Result.DENY);
    }

    @SubscribeEvent
    public static void livingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (!entity.level.isClientSide && Float.isNaN(entity.getHealth())) entity.setHealth(0.0F);
    }

    @SubscribeEvent
    public static void livingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (!entity.level.isClientSide && Float.isNaN(entity.getHealth())) entity.setHealth(0.0F);
    }
}

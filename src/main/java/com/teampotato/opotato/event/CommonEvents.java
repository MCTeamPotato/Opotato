package com.teampotato.opotato.event;

import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.versions.forge.ForgeVersion;

import java.util.Arrays;
import java.util.UUID;

import static com.teampotato.opotato.util.opotato.EventUtil.*;

@Mod.EventBusSubscriber(modid = Opotato.ID)
public class CommonEvents {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        Opotato.OpotatoCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!(event.getWorld() instanceof ServerWorld)) return;
        Entity entity = event.getEntity();
        if (entity instanceof ServerPlayerEntity) return;
        ServerWorld world = (ServerWorld) event.getWorld();
        Entity existing = world.getEntity(entity.getUUID());
        if (existing == null || existing == entity) return;
        UUID newUUID = MathHelper.createInsecureUUID();
        while (world.getEntity(newUUID) != null) newUUID = MathHelper.createInsecureUUID();
        entity.setUUID(newUUID);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        boolean rb = isLoaded("rubidium");
        if (isLoaded("epicfight")) {
            if (!ForgeVersion.getVersion().equals("36.2.39") && ModList.get().getModFileById("epicfight").getFile().getFileName().contains("16.6.4"))
                addIncompatibleWarn(event, "opotato.epicfight.wrong_forge_version");
        }
        if (rb) {
            if (isLoaded("betterfpsdist")) addIncompatibleWarn(event, "opotato.incompatible.rubidium.betterfpsdist");
            if (isLoaded("immersive_portals")) addIncompatibleWarn(event, "opotato.incompatible.rubidium.immersive_portals");
            if (isLoaded("chunkanimator"))addIncompatibleWarn(event, "opotato.incompatible.rubidium.chunkanimator");
        }
        if (isLoaded("mcdoom") && !isLoaded("mcdoomfix")) addIncompatibleWarn(event, "opotato.mcdoom.without_fix");
        if (isLoaded("magnesium")) {
            if (rb) {
                addIncompatibleWarn(event, "opotato.incompatible.magnesium.rubidium");
            } else {
                addIncompatibleWarn(event, "opotato.magnesium");
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntityLiving();
        World world = entity.level;
        MinecraftServer server = world.getServer();
        ResourceLocation name = entity.getType().getRegistryName();
        if (!PotatoCommonConfig.KILL_WITHER_STORM_MOD_ENTITIES_AFTER_COMMAND_BLOCK_DIES.get() || world.isClientSide || name == null || server == null || !name.toString().equals("witherstormmod:command_block")) return;
        Arrays
                .stream(new String[]{"block_cluster", "sickened_skeleton", "sickened_creeper", "sickened_spider", "sickened_zombie", "tentacle", "withered_symbiont"})
                .forEach(obj -> exeCmd(server, "/kill @e[type=witherstormmod:" + obj + "]"));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void ctrlSpawn(LivingSpawnEvent.CheckSpawn event) {
        LivingEntity entity = event.getEntityLiving();
        IWorld world = event.getWorld();
        ResourceLocation regName = entity.getType().getRegistryName();
        if (!PotatoCommonConfig.ALLOW_LIMIT_MAX_SPAWN.get() || regName == null || world.isClientSide() || PotatoCommonConfig.BLACKLIST.get().contains(regName.toString())) return;
        ChunkPos chunk = world.getChunk(entity.blockPosition()).getPos();
        if (world.getEntitiesOfClass(entity.getClass(), new AxisAlignedBB(chunk.getMinBlockX(), 0, chunk.getMinBlockZ(), chunk.getMaxBlockX(), 256, chunk.getMaxBlockX())).size() > PotatoCommonConfig.MAX_ENTITIES_NUMBER_PER_CHUNK.get()) event.setResult(Event.Result.DENY);
    }
}

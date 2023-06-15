package com.teampotato.opotato.event;

import com.google.common.collect.Lists;
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
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.versions.forge.ForgeVersion;

import java.util.List;
import java.util.UUID;

import static com.teampotato.opotato.Opotato.*;

@Mod.EventBusSubscriber(modid = Opotato.ID)
public class PotatoEvents {
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Entity source = event.getSource().getDirectEntity();
        if (!(source instanceof ServerPlayerEntity) || !PotatoCommonConfig.ENABLE_CREATIVE_ONE_POUCH.get()) return;
        ServerPlayerEntity player = (ServerPlayerEntity) source;
        if (!player.isCreative()) return;
        event.getEntityLiving().setHealth(0.0F);
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

    private static final boolean USING_OPTIFINE;

    static {
        boolean hasOfClass = false;
        try {
            Class.forName("optifine.OptiFineTransformationService");
            hasOfClass = true;
        } catch (ClassNotFoundException ignored) {}
        USING_OPTIFINE = hasOfClass;
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event)  {
        if (USING_OPTIFINE) addIncompatibleWarn(event, "opotato.optnotfine");
        boolean rb = isLoaded("rubidium");
        if (isLoaded("epicfight") && !ForgeVersion.getVersion().equals("36.2.39") && ModList.get().getModFileById("epicfight").getFile().getFileName().contains("16.6.4")) {
            addIncompatibleWarn(event, "opotato.epicfight.wrong_forge_version");
        }
        if (rb) {
            if (isLoaded("betterfpsdist")) addIncompatibleWarn(event, "opotato.incompatible.rubidium.betterfpsdist");
            if (isLoaded("immersive_portals")) addIncompatibleWarn(event, "opotato.incompatible.rubidium.immersive_portals");
            if (isLoaded("chunkanimator")) addIncompatibleWarn(event, "opotato.incompatible.rubidium.chunkanimator");
            if (isLoaded("betterbiomeblend")) addIncompatibleWarn(event, "opotato.incompatible.rubidium.betterbiomeblend");
        }
        if (isLoaded("mcdoom") && !isLoaded("mcdoomfix")) addIncompatibleWarn(event, "opotato.mcdoom.without_fix");
        if (isLoaded("magnesium")) {
            if (rb) {
                addIncompatibleWarn(event, "opotato.incompatible.magnesium.rubidium");
            } else {
                addIncompatibleWarn(event, "opotato.magnesium");
            }
        }
        if (isLoaded("potatocurrent")) addIncompatibleWarn(event, "opotato.duplicate.potatocurrent");
        if (isLoaded("cataclysmfixer")) addIncompatibleWarn(event, "opotato.duplicate.cataclysmfixer");
        if (isLoaded("mixininheaven")) addIncompatibleWarn(event, "opotato.duplicate.mixininheaven");
        if (isLoaded("blueprintinternetconnectiondisabler")) addIncompatibleWarn(event, "opotato.duplicate.blueprintinternetconnectiondisabler");
    }

    private static final List<String> targets = Lists.newArrayList("block_cluster", "sickened_skeleton", "sickened_creeper", "sickened_spider", "sickened_zombie", "tentacle", "withered_symbiont");

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntityLiving();
        World world = entity.level;
        MinecraftServer server = world.getServer();
        ResourceLocation name = entity.getType().getRegistryName();
        if (!PotatoCommonConfig.KILL_WITHER_STORM_MOD_ENTITIES_AFTER_COMMAND_BLOCK_DIES.get() || world.isClientSide || name == null || server == null || !name.toString().equals("witherstormmod:command_block")) return;
        for (String target : targets) {
            exeCmd(server, "/kill @e[type=witherstormmod:" + target + "]");
        }
    }

    @SubscribeEvent
    public static void ctrlSpawn(LivingSpawnEvent.CheckSpawn event) {
        LivingEntity entity = event.getEntityLiving();
        IWorld world = event.getWorld();
        ResourceLocation regName = entity.getType().getRegistryName();
        if (!PotatoCommonConfig.ALLOW_LIMIT_MAX_SPAWN.get() || regName == null || world.isClientSide() || PotatoCommonConfig.BLACKLIST.get().contains(regName.toString())) return;
        ChunkPos chunk = world.getChunk(entity.blockPosition()).getPos();
        if (world.getEntitiesOfClass(entity.getClass(), new AxisAlignedBB(
                chunk.getMinBlockX(),
                0,
                chunk.getMinBlockZ(),
                chunk.getMaxBlockX(),
                256,
                chunk.getMaxBlockX()
        )).size() > PotatoCommonConfig.MAX_ENTITIES_NUMBER_PER_CHUNK.get()) event.setResult(Event.Result.DENY);
    }
}

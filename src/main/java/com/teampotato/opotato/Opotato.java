package com.teampotato.opotato;

import com.teampotato.opotato.config.json.PotatoJsonConfig;
import com.teampotato.opotato.config.mods.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.versions.forge.ForgeVersion;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.teampotato.opotato.EarlySetupInitializer.isLoaded;

@Mod(EarlySetupInitializer.MOD_ID)
public class Opotato {

    public Opotato() {
        if (PotatoJsonConfig.initFailed) {
            if (PotatoJsonConfig.readException != null) EarlySetupInitializer.LOGGER.error("Failed to read Opotato json config", PotatoJsonConfig.readException);
            if (PotatoJsonConfig.writeException != null) EarlySetupInitializer.LOGGER.error("Failed to write Opotato json config", PotatoJsonConfig.writeException);
            throw new RuntimeException("Error occurs during Opotato json config initialization");
        }
        initConfigs(ModLoadingContext.get());
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        initEvents(forgeBus, modBus);
        if (FMLLoader.getDist().isClient()) OpotatoClient.initClientEvents(forgeBus, modBus);
    }

    private static void initEvents(@NotNull IEventBus forgeBus, IEventBus modBus) {

        forgeBus.addListener(EventPriority.LOWEST, (LivingHurtEvent event) -> {
            Entity source = event.getSource().getDirectEntity();
            if (source instanceof ServerPlayer && EarlySetupInitializer.potatoJsonConfig.enableCreativeOnePouch && EarlySetupInitializer.creativeOnePunch && !event.isCanceled()) {
                ServerPlayer player = (ServerPlayer) source;
                if (player.isCreative()) event.getEntityLiving().setHealth(0.0F);
            }
        });

        forgeBus.addListener(EventPriority.LOW, (EntityJoinWorldEvent event) -> {
            if (event.getWorld() instanceof ServerLevel && !event.isCanceled()) {
                Entity entity = event.getEntity();
                if (entity instanceof ServerPlayer) return;
                ServerLevel serverLevel = (ServerLevel) event.getWorld();
                Entity existing = serverLevel.getEntity(entity.getUUID());
                if (existing == null || existing == entity) return;
                UUID newUUID = Mth.createInsecureUUID(ThreadLocalRandom.current());
                while (serverLevel.getEntity(newUUID) != null) newUUID = Mth.createInsecureUUID(ThreadLocalRandom.current());
                entity.setUUID(newUUID);
            }
        });

        if (EarlySetupInitializer.potatoJsonConfig.showModCompatibilityWarning) {
            modBus.addListener((FMLCommonSetupEvent event) -> {
                if (isLoaded("epicfight") && !ForgeVersion.getVersion().equals("36.2.39") && ModList.get().getModFileById("epicfight").getFile().getFileName().contains("16.6.4")) {
                    EarlySetupInitializer.addIncompatibleWarn(event, "opotato.epicfight.wrong_forge_version");
                }
                if (EarlySetupInitializer.isRubidiumLoaded) {
                    if (isLoaded("betterfpsdist")) EarlySetupInitializer.addIncompatibleWarn(event, "opotato.incompatible.rubidium.betterfpsdist");
                    if (isLoaded("immersive_portals")) EarlySetupInitializer.addIncompatibleWarn(event, "opotato.incompatible.rubidium.immersive_portals");
                    if (isLoaded("chunkanimator")) EarlySetupInitializer.addIncompatibleWarn(event, "opotato.incompatible.rubidium.chunkanimator");
                    if (isLoaded("betterbiomeblend")) EarlySetupInitializer.addIncompatibleWarn(event, "opotato.incompatible.rubidium.betterbiomeblend");
                }
                if (isLoaded("mcdoom") && !isLoaded("mcdoomfix")) EarlySetupInitializer.addIncompatibleWarn(event, "opotato.mcdoom.without_fix");
                if (isLoaded("magnesium")) {
                    if (EarlySetupInitializer.isRubidiumLoaded) {
                        EarlySetupInitializer.addIncompatibleWarn(event, "opotato.incompatible.magnesium.rubidium");
                    } else {
                        EarlySetupInitializer.addIncompatibleWarn(event, "opotato.magnesium");
                    }
                }
                if (isLoaded("helium")) EarlySetupInitializer.addIncompatibleWarn(event, "opotato.helium.dangerous");
            });
        }

        if (EarlySetupInitializer.isWitherStormModLoaded) {
            forgeBus.addListener(EventPriority.LOWEST, (LivingDeathEvent event) -> {
                if (!WitherStormExtraConfig.killAllWitherStormModEntitiesWhenTheCommandBlockDies.get() || event.isCanceled()) return;
                LivingEntity entity = event.getEntityLiving();
                if (entity instanceof CommandBlockEntity && entity.level instanceof ServerLevel) {
                    MinecraftServer server = ((ServerLevel) entity.level).getServer();
                    for (String target : EarlySetupInitializer.WITHER_STORM_CLEANER_TARGETS) {
                        server.getCommands().performCommand(server.createCommandSourceStack().withSuppressedOutput(), "/kill @e[type=witherstormmod:" + target + "]");
                    }
                }
            });
        }
    }

    private static void initConfigs(ModLoadingContext ctx) {
        ModConfig.Type common = ModConfig.Type.COMMON;
        if (EarlySetupInitializer.isLoaded("ars_nouveau")) ctx.registerConfig(common, ArsNouveauLootConfig.arsNouveauConfig, EarlySetupInitializer.MOD_ID + "/mods/arsNouveau-loot.toml");
        if (EarlySetupInitializer.isLoaded("blue_skies")) ctx.registerConfig(common, BlueSkiesExtraConfig.blueSkiesExtraConfig, EarlySetupInitializer.MOD_ID + "/mods/blueSkies-extra.toml");
        if (EarlySetupInitializer.isCataclysmLoaded) ctx.registerConfig(common, CataclysmExtraConfig.cataclysmExtraConfig, EarlySetupInitializer.MOD_ID + "/mods/cataclysm-extra.toml");
        if (EarlySetupInitializer.isLoaded("headshot")) ctx.registerConfig(common, HeadshotExtraConfig.headshotConfig, EarlySetupInitializer.MOD_ID + "/mods/headshot-extra.toml");
        if (EarlySetupInitializer.isLoaded("undergarden")) ctx.registerConfig(common, UndergardenExtraConfig.undergardenConfig, EarlySetupInitializer.MOD_ID + "/mods/undergarden-extra.toml");
        if (EarlySetupInitializer.isWitherStormModLoaded) ctx.registerConfig(common, WitherStormExtraConfig.witherStormConfig, EarlySetupInitializer.MOD_ID + "/mods/witherStormMod-extra.toml");
    }
}

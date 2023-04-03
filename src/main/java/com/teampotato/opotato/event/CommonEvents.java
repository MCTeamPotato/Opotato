package com.teampotato.opotato.event;

import com.teampotato.opotato.Opotato;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.versions.forge.ForgeVersion;

import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber
public class CommonEvents {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        Opotato.OpotatoCommand.register(event.getDispatcher());
    }

    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void duplicateEntityUUIDFix(EntityJoinWorldEvent event) {
        if (event.getWorld() instanceof ServerWorld) {
            Entity entity = event.getEntity();
            if (entity instanceof PlayerEntity) return;
            ServerWorld world = (ServerWorld) event.getWorld();
            Entity existing = world.getEntity(entity.getUUID());
            if (existing != null && existing != entity) {
                UUID newUUID = MathHelper.createInsecureUUID(RANDOM);
                while (world.getEntity(newUUID) != null) newUUID = MathHelper.createInsecureUUID(RANDOM);
                entity.setUUID(newUUID);
            }
        }
    }

    @SubscribeEvent
    public void forgeVersionCheck(FMLCommonSetupEvent event) {
        if (!ForgeVersion.getVersion().equals("36.2.39") && ModList.get().isLoaded("epicfight") && ModList.get().getModFileById("epicfight").getFile().getFileName().contains("16.6.4")) {
            event.enqueueWork(() -> ModLoader.get().addWarning(new ModLoadingWarning(ModLoadingContext.get().getActiveContainer().getModInfo(), ModLoadingStage.COMMON_SETUP, "opotato.epicfight.wrong_forge_version")));
        }
    }
}

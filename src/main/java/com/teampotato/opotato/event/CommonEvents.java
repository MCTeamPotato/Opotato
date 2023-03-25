package com.teampotato.opotato.event;

import com.teampotato.opotato.Opotato;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
            ServerWorld world = (ServerWorld) event.getWorld();
            Entity entity = event.getEntity();
            if (entity instanceof PlayerEntity) return;
            Entity existing = world.getEntity(entity.getUUID());
            if (existing != null && existing != entity) {
                UUID newUUID = MathHelper.createInsecureUUID(RANDOM);
                while (world.getEntity(newUUID) != null) newUUID = MathHelper.createInsecureUUID(RANDOM);
                entity.setUUID(newUUID);
            }
        }
    }
}

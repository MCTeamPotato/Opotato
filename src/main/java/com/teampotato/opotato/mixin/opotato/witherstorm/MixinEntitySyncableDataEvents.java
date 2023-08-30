package com.teampotato.opotato.mixin.opotato.witherstorm;

import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.events.WitherStormCaches;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.network.PacketDistributor;
import nonamecrackers2.witherstormmod.common.event.EntitySyncableDataEvents;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.packet.EntitySyncableDataMessage;
import nonamecrackers2.witherstormmod.common.util.IEntitySyncableData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.UUID;

@Mixin(value = EntitySyncableDataEvents.class, remap = false)
public abstract class MixinEntitySyncableDataEvents {
    /**
     * @author Kasualix
     * @reason impl cache
     */
    @Overwrite
    public static void sendChanges(ServerPlayer player) {
        ServerLevel world = player.getLevel();
        for (UUID uuid : WitherStormCaches.dataSyncableEntities) {
            Entity entity = world.getEntity(uuid);
            if (entity != null) {
                EntitySyncableDataMessage message = new EntitySyncableDataMessage(entity.getId(), (IEntitySyncableData) entity);
                WitherStormModPacketHandlers.MAIN.send(PacketDistributor.PLAYER.with(() -> player), message);
            } else {
                Opotato.LOGGER.error("What's up? dataSyncableEntities contains entities that is not in the world!");
                WitherStormCaches.dataSyncableEntities.remove(uuid);
            }
        }
    }
}
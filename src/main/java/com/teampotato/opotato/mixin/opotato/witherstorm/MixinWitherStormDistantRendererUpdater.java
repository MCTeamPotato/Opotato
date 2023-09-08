package com.teampotato.opotato.mixin.opotato.witherstorm;

import com.teampotato.opotato.events.EntitiesCacheEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.network.PacketDistributor;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.packet.CreateDebrisMessage;
import nonamecrackers2.witherstormmod.common.packet.WitherStormToDistantRendererMessage;
import nonamecrackers2.witherstormmod.common.util.WitherStormDistantRendererUpdater;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.UUID;

@Mixin(value = WitherStormDistantRendererUpdater.class, remap = false)
public abstract class MixinWitherStormDistantRendererUpdater {
    /**
     * @author Kasualix
     * @reason impl cache and optimization
     */
    @Overwrite
    public static void sendInformationToDistantRenderer(@NotNull ServerPlayer player) {
        ServerLevel world = player.getLevel();

        for (UUID uuid : EntitiesCacheEvent.witherStorms.keySet()) {
            WitherStormEntity entity = (WitherStormEntity) world.getEntity(uuid);
            if (entity != null) {
                WitherStormToDistantRendererMessage witherStormMessage = new WitherStormToDistantRendererMessage(entity);
                WitherStormModPacketHandlers.MAIN.send(PacketDistributor.PLAYER.with(() -> player), witherStormMessage);
                entity.getPlayDeadManager().sendChanges(PacketDistributor.PLAYER.with(() -> player), false);
                CreateDebrisMessage createDebrisMessage = new CreateDebrisMessage(entity, entity.isDeadOrPlayingDead());
                WitherStormModPacketHandlers.MAIN.send(PacketDistributor.PLAYER.with(() -> player), createDebrisMessage);
            }
        }

    }
}

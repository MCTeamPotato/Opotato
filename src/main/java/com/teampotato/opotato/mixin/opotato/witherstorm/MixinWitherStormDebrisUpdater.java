package com.teampotato.opotato.mixin.opotato.witherstorm;

import com.teampotato.opotato.events.WitherStormCaches;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.network.PacketDistributor;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.event.WitherStormDebrisUpdater;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.packet.CreateDebrisMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.UUID;

@Mixin(value = WitherStormDebrisUpdater.class, remap = false)
public abstract class MixinWitherStormDebrisUpdater {
    /**
     * @author Kasualix
     * @reason impl cache and optimization
     */
    @Overwrite
    public static void createDebris(ServerPlayer player) {
        ServerLevel world = player.getLevel();
        for (UUID uuid : WitherStormCaches.witherStorms.keySet()) {
            WitherStormEntity storm = (WitherStormEntity) world.getEntity(uuid);
            if (storm == null) continue;
            WitherStormModPacketHandlers.MAIN.send(PacketDistributor.PLAYER.with(() -> player), new CreateDebrisMessage(storm, storm.isDeadOrPlayingDead()));
        }
    }
}

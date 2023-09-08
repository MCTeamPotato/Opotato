package com.teampotato.opotato.mixin.opotato.witherstorm;

import com.teampotato.opotato.events.EntitiesCacheEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.network.PacketDistributor;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.event.WitherStormSoundLoopingManagerUpdater;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.packet.CreateLoopingSoundMessage;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.UUID;

@Mixin(WitherStormSoundLoopingManagerUpdater.class)
public abstract class MixinWitherStormSoundLoopingManagerUpdater {
    /**
     * @author Kasualix
     * @reason impl cache and optimization
     */
    @Overwrite
    public static void sendInformationToManager(@NotNull ServerPlayer player) {
        ServerLevel world = player.getLevel();
        for (UUID uuid : EntitiesCacheEvent.witherStorms.keySet()) {
            WitherStormEntity storm = (WitherStormEntity) world.getEntity(uuid);
            if (storm != null && storm.shouldPlaySoundLoops()) {
                WitherStormModPacketHandlers.MAIN.send(PacketDistributor.PLAYER.with(() -> player), new CreateLoopingSoundMessage(storm));
            }
        }
    }
}

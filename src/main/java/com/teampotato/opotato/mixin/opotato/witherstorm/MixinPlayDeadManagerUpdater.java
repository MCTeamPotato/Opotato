package com.teampotato.opotato.mixin.opotato.witherstorm;

import com.teampotato.opotato.events.EntitiesCacheEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.network.PacketDistributor;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.event.PlayDeadManagerUpdater;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.UUID;

@Mixin(value = PlayDeadManagerUpdater.class, remap = false)
public abstract class MixinPlayDeadManagerUpdater {
    /**
     * @author Kasualix
     * @reason impl cache and optimization
     */
    @Overwrite

    public static void sendChanges(@NotNull ServerPlayer player) {
        ServerLevel world = player.getLevel();
        for (UUID uuid : EntitiesCacheEvent.witherStorms.keySet()){
            WitherStormEntity witherStormEntity = (WitherStormEntity) world.getEntity(uuid);
            if (witherStormEntity == null) return;
            witherStormEntity.getPlayDeadManager().sendChanges(PacketDistributor.PLAYER.with(() -> player), false);
        }
    }
}

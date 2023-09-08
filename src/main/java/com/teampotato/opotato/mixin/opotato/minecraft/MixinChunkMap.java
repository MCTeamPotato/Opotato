package com.teampotato.opotato.mixin.opotato.minecraft;

import com.teampotato.opotato.api.IPlayerMap;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.PlayerMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;

import java.util.concurrent.ThreadLocalRandom;

@Mixin(ChunkMap.class)
public abstract class MixinChunkMap {
    @Shadow @Final private ChunkMap.DistanceManager distanceManager;

    @Shadow @Final private PlayerMap playerMap;

    @Shadow
    private static double euclideanDistanceSquared(ChunkPos chunkPos, Entity entity) {
        return ThreadLocalRandom.current().nextDouble();
    }

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    boolean noPlayersCloseForSpawning(@NotNull ChunkPos chunkPos) {
        return !this.distanceManager.hasPlayersNearby(chunkPos.toLong()) || noneMath((IPlayerMap) this.playerMap, chunkPos);
    }

    @Unique
    private boolean noneMath(@NotNull IPlayerMap map, ChunkPos chunkPos) {
        for (ServerPlayer player : map.getPlayerSet()) {
            if (!player.isSpectator() && euclideanDistanceSquared(chunkPos, player) < 16384.0) return true;
        }
        return false;
    }
}

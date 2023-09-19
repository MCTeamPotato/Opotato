package com.teampotato.opotato.mixin.api;

import com.teampotato.opotato.api.IChunkMap;
import com.teampotato.opotato.api.IPlayerMap;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.PlayerMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChunkMap.class)
public  class MixinChunkMap implements IChunkMap {
    @Shadow @Final private PlayerMap playerMap;

    @Shadow
    private static int checkerboardDistance(ChunkPos arg, ServerPlayer arg2, boolean bl) {
        throw new RuntimeException();
    }

    @Shadow private int viewDistance;

    @Override
    public boolean potato$hasNoPlayers(ChunkPos pos, boolean boundaryOnly) {
        for (ServerPlayer player : ((IPlayerMap)this.playerMap).potato$getPlayerSet()) {
            int i = checkerboardDistance(pos, player, true);
            if (i <= this.viewDistance && (!boundaryOnly || i == this.viewDistance)) return false;
        }
        return true;
    }
}

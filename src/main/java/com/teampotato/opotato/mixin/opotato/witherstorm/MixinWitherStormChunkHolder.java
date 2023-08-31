package com.teampotato.opotato.mixin.opotato.witherstorm;

import com.teampotato.opotato.api.IChunkMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import nonamecrackers2.witherstormmod.common.capability.WitherStormChunkHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mixin(WitherStormChunkHolder.class)
public abstract class MixinWitherStormChunkHolder {

    @Shadow(remap = false) private List<ChunkPos> allLoadedChunks;

    @Shadow @Final private ServerLevel world;

    @Shadow(remap = false) private Map<UUID, BlockPos> stormPositions;

    @Shadow(remap = false) private Map<UUID, List<ChunkPos>> chunksLoaded;

    @Inject(method = {"<init>()V", "<init>(Lnet/minecraft/server/level/ServerLevel;)V"}, at = @At("RETURN"), remap = false)
    private void onInit(CallbackInfo ci) {
        this.allLoadedChunks = new ObjectArrayList<>(this.allLoadedChunks);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getInt(Lnet/minecraft/world/level/GameRules$Key;)I"))
    private int disableOriginChunkTickLogic(GameRules instance, GameRules.Key<GameRules.IntegerValue> key) {
        return 0;
    }

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnonamecrackers2/witherstormmod/common/capability/WitherStormChunkHolder;firstTick:Z", shift = At.Shift.BEFORE, remap = false), remap = false)
    private void tickChunks(CallbackInfo ci) {
        int tickSpeed = this.world.getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
        if (tickSpeed > 0) {
            for (Map.Entry<UUID, List<ChunkPos>> entry : this.chunksLoaded.entrySet()) {
                List<ChunkPos> loadedChunks = entry.getValue();

                for (ChunkPos pos : loadedChunks) {
                    if (((IChunkMap)this.world.getChunkSource().chunkMap).hasNotPlayers(pos, false)) {
                        this.world.tickChunk(this.world.getChunk(pos.x, pos.z), tickSpeed);
                    }
                }

                if (!this.stormPositions.containsKey(entry.getKey()) && loadedChunks.isEmpty()) this.chunksLoaded.remove(entry.getKey());
            }
        }
    }
}

package com.teampotato.opotato.mixin.opotato.witherstorm;

import com.teampotato.opotato.events.WitherStormCacheEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.event.WitherStormChunkLoader;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Map;
import java.util.UUID;

@Mixin(value = WitherStormChunkLoader.class, remap = false)
public abstract class MixinWitherStormChunkLoader {
    /**
     * @author Kasualix
     * @reason optimize
     */
    @Overwrite
    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.world instanceof ServerLevel) {
            ServerLevel world = (ServerLevel) event.world;
            world.getCapability(WitherStormModCapabilities.WITHER_STORM_CHUNKS_CAPABILITY).ifPresent(stormChunks -> {
                if (!world.players().isEmpty()) {
                    boolean flag = true;
                    for (Map.Entry<UUID, BlockPos> uuidBlockPosEntry : stormChunks.getStormPositions().entrySet()) {
                        UUID uuid = uuidBlockPosEntry.getKey();
                        if (WitherStormCacheEvents.witherStormUUID != null){
                            WitherStormEntity entity = (WitherStormEntity) world.getEntity(WitherStormCacheEvents.witherStormUUID);
                            if (entity != null && entity.getUUID().equals(uuid)) {
                                if (!entity.isDeadOrDying()) {
                                    ChunkPos prevChunk = world.getChunk(stormChunks.getPrevStormPositions().get(uuid)).getPos();
                                    ChunkPos chunk = world.getChunk(stormChunks.getStormPositions().get(uuid)).getPos();
                                    if (chunk != prevChunk) stormChunks.createChunkQueue(entity, chunk, true);
                                    stormChunks.updateStormPosition(entity);
                                } else if (entity.isDeadOrDying() || !entity.isAddedToWorld()) {
                                    stormChunks.removeStorm(entity);
                                }
                                flag = false;
                            }
                        }

                        if (flag) stormChunks.removeStorm(uuid);
                    }
                }

                stormChunks.tick();
            });
        }
    }
}

package com.teampotato.opotato.mixin.opotato.witherstormmod;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.capability.WitherStormChunkHolder;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.event.WitherStormChunkLoader;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Mixin(value = WitherStormChunkLoader.class, remap = false)
public abstract class MixinWitherStormChunkLoader {
    /**
     * @author Kasualix
     * @reason Optimize event
     */
    @Overwrite
    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.world instanceof ServerWorld)) return;
        ServerWorld world = (ServerWorld) event.world;
        world.getCapability(WitherStormModCapabilities.WITHER_STORM_CHUNKS_CAPABILITY)
                .ifPresent(stormChunkHolder -> handleStromChunkHoldLogic(world, stormChunkHolder));
    }

    private static void handleStromChunkHoldLogic(ServerWorld world, WitherStormChunkHolder stormChunkHolder) {
        boolean flag = true;
        if (world.getServer().getPlayerCount() == 0) {
            stormChunkHolder.tick();
            return;
        }

        Set<Map.Entry<UUID, BlockPos>> posSet = stormChunkHolder.getStormPositions().entrySet();
        if (posSet.size() == 1) {
            UUID uuid = findFirst(posSet);
            for (Entity entity : world.getAllEntities()) {
                boolean equal = entity.getUUID().equals(uuid);
                if (equal) flag = false;
                if (entity instanceof WitherStormEntity && equal) {
                    WitherStormEntity witherStormEntity = (WitherStormEntity) entity;
                    if (witherStormEntity.isDeadOrDying()) {
                        ChunkPos chunk = world.getChunk(stormChunkHolder.getStormPositions().get(uuid)).getPos();
                        if (chunk != world.getChunk(stormChunkHolder.getPrevStormPositions().get(uuid)).getPos()) stormChunkHolder.createChunkQueue(witherStormEntity, chunk, true);
                        stormChunkHolder.updateStormPosition(witherStormEntity);
                    } else if ((witherStormEntity.isDeadOrDying() || !entity.isAddedToWorld())) {
                        stormChunkHolder.removeStorm(witherStormEntity);
                    }
                }
            }

            if (flag) stormChunkHolder.removeStorm(uuid);
            stormChunkHolder.tick();
            return;
        }

        //Multiple Wither Storms? Are you sure???
        for (Map.Entry<UUID, BlockPos> uuidBlockPosEntry : posSet) {
            UUID uuid = uuidBlockPosEntry.getKey();
            for (Entity entity : world.getAllEntities()) {
                if (entity.getUUID().equals(uuid)) flag = false;
                WitherStormEntity storm;
                if (entity instanceof WitherStormEntity && entity.getUUID().equals(uuid) && !((WitherStormEntity) entity).isDeadOrDying()) {
                    storm = (WitherStormEntity) entity;
                    ChunkPos chunk = world.getChunk(stormChunkHolder.getStormPositions().get(uuid)).getPos();
                    if (chunk != world.getChunk(stormChunkHolder.getPrevStormPositions().get(uuid)).getPos()) stormChunkHolder.createChunkQueue(storm, chunk, true);
                    stormChunkHolder.updateStormPosition(storm);
                } else if (entity instanceof WitherStormEntity && entity.getUUID().equals(uuid) && (((WitherStormEntity) entity).isDeadOrDying() || !entity.isAddedToWorld())) {
                    storm = (WitherStormEntity) entity;
                    stormChunkHolder.removeStorm(storm);
                }
            }
            if (flag) stormChunkHolder.removeStorm(uuid);
        }
        stormChunkHolder.tick();
    }

    private static UUID findFirst(Set<Map.Entry<UUID, BlockPos>> posSet) {
        for (Map.Entry<UUID, BlockPos> entry : posSet) {
            return entry.getKey();
        }
        return null;
    }
}

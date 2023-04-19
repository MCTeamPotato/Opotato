package com.teampotato.opotato.util.opotato;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.profiler.IProfiler;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.common.capability.WitherSicknessTracker;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import static com.teampotato.opotato.config.PotatoCommonConfig.*;

public class MixinUtil {
    public static class MinecraftUtil {
        public static <T extends Entity> void getEntitiesOfClass(Class<? extends T> cs, AxisAlignedBB aabb, List<T> list, @Nullable Predicate<? super T> predicate, World level, ClassInheritanceMultiMap<Entity>[] entitySections) {
            for(int k = MathHelper.clamp(MathHelper.floor((aabb.minY - level.getMaxEntityRadius()) / 16.0D), 0, entitySections.length - 1);
                k <= MathHelper.clamp(MathHelper.floor((aabb.maxY + level.getMaxEntityRadius()) / 16.0D), 0, entitySections.length - 1); ++k) {
                entitySections[k].find(cs).forEach(t -> {
                    if (t.getBoundingBox().intersects(aabb) && (predicate == null || predicate.test(t))) list.add(t);
                });
            }
        }

        public static <T extends Entity> @NotNull List<T> getEntitiesOfClass(@NotNull Class<? extends T> cs, AxisAlignedBB aabb, @Nullable Predicate<? super T> predicate, IProfiler profiler, double maxEntityRadius, AbstractChunkProvider chunkSource) {
            profiler.incrementCounter("getEntities");
            List<T> list = Lists.newArrayList();
            for(int i1 = MathHelper.floor((aabb.minX - maxEntityRadius) / 16.0D); i1 < MathHelper.ceil((aabb.maxX + maxEntityRadius) / 16.0D); ++i1) {
                for(int j1 = MathHelper.floor((aabb.minZ - maxEntityRadius) / 16.0D); j1 < MathHelper.ceil((aabb.maxZ + maxEntityRadius) / 16.0D); ++j1) {
                    Chunk chunk = chunkSource.getChunk(i1, j1, false);
                    if (chunk != null) chunk.getEntitiesOfClass(cs, aabb, list, predicate);
                }
            }
            return list;
        }
    }

    public static class WitherStormUtil {
        public static void playersHandle(World level, Collection<Entity> storms) {
            level.players().stream()
                    .filter(player -> player != null && canBeSicken(player))
                    .forEach(player -> player.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER).ifPresent(tracker -> capHandle(tracker, player, storms)));
        }

        public static void allEntitiesHandle(World level, Collection<Entity> storms) {
            StreamSupport.stream(((ServerWorld) level).getAllEntities().spliterator(), false)
                    .filter(entity -> entity instanceof LivingEntity && canBeSicken(entity))
                    .filter(entity -> !(entity instanceof MonsterEntity) && LET_MOBS_IMMUNE_TO_WITHER_SICKNESS_TICKING_SYSTEM.get())
                    .filter(entity -> !(entity instanceof AnimalEntity) && LET_ANIMALS_IMMUNE_TO_WITHER_SICKNESS_TICKING_SYSTEM.get())
                    .forEach(living -> living.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER).ifPresent(tracker -> capHandle(tracker, living, storms)));
        }

        private static boolean canBeSicken(Entity entity) {
            return entity.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER).isPresent();
        }

        private static void capHandle(WitherSicknessTracker tracker, Entity living, Collection<Entity> storms) {
            if (!tracker.isActuallyImmune()) {
                boolean nearby = living.level.dimension().location().equals(WitherStormMod.bowelsLocation());
                if (!nearby && storms.stream().anyMatch(storm -> storm instanceof WitherStormEntity && ((WitherStormEntity)storm).isEntityNearby(living))) nearby = true;
                tracker.setNearStorm(nearby);
            }
            tracker.tick();
        }

        public static void chunkWithWitherStormLoad(ServerWorld world) {
            world.getCapability(WitherStormModCapabilities.WITHER_STORM_CHUNKS_CAPABILITY).ifPresent((stormChunks) -> {
                if (world.getServer().getPlayerCount() > 0) {
                    boolean flag = true;
                    for (Map.Entry<UUID, BlockPos> uuidBlockPosEntry : stormChunks.getStormPositions().entrySet()) {
                        UUID uuid = uuidBlockPosEntry.getKey();
                        for (Entity entity : world.getAllEntities()) {
                            WitherStormEntity storm;
                            if (entity instanceof WitherStormEntity && entity.getUUID().equals(uuid) && !((WitherStormEntity) entity).isDeadOrDying()) {
                                storm = (WitherStormEntity) entity;
                                ChunkPos prevChunk = world.getChunk(stormChunks.getPrevStormPositions().get(uuid)).getPos();
                                ChunkPos chunk = world.getChunk(stormChunks.getStormPositions().get(uuid)).getPos();
                                if (chunk != prevChunk) stormChunks.createChunkQueue(storm, chunk, true);
                                stormChunks.updateStormPosition(storm);
                            } else if (entity instanceof WitherStormEntity && entity.getUUID().equals(uuid) && (((WitherStormEntity) entity).isDeadOrDying() || !entity.isAddedToWorld())) {
                                storm = (WitherStormEntity) entity;
                                stormChunks.removeStorm(storm);
                            }
                            if (entity.getUUID().equals(uuid)) flag = false;
                        }
                        if (flag) stormChunks.removeStorm(uuid);
                    }
                }
                stormChunks.tick();
            });
        }
    }
}

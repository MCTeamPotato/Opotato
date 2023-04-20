package com.teampotato.opotato.util.opotato;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.profiler.IProfiler;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class MixinUtil {
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

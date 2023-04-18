package com.teampotato.opotato.mixin.opotato;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.profiler.IProfiler;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

@Mixin(World.class)
public abstract class MixinWorld implements IWorld {
    @Shadow public abstract IProfiler getProfiler();
    @Shadow(remap = false) public abstract double getMaxEntityRadius();

    /**
     * @author Kasuliax
     * @reason see the performance impact of getEntitiesOfClass
     */
    @Overwrite
    public <T extends Entity> @NotNull List<T> getEntitiesOfClass(@NotNull Class<? extends T> cs, AxisAlignedBB aabb, @Nullable Predicate<? super T> predicate) {
        this.getProfiler().incrementCounter("getEntities");
        List<T> list = Lists.newArrayList();
        for(int i1 = MathHelper.floor((aabb.minX - getMaxEntityRadius()) / 16.0D); i1 < MathHelper.ceil((aabb.maxX + getMaxEntityRadius()) / 16.0D); ++i1) {
            for(int j1 = MathHelper.floor((aabb.minZ - getMaxEntityRadius()) / 16.0D); j1 < MathHelper.ceil((aabb.maxZ + getMaxEntityRadius()) / 16.0D); ++j1) {
                Chunk chunk = this.getChunkSource().getChunk(i1, j1, false);
                if (chunk != null) chunk.getEntitiesOfClass(cs, aabb, list, predicate);
            }
        }
        return list;
    }
}

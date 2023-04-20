package com.teampotato.opotato.mixin.opotato.minecraft;

import com.teampotato.opotato.util.opotato.MixinUtil;
import net.minecraft.entity.Entity;
import net.minecraft.profiler.IProfiler;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
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
    public <T extends Entity> @NotNull List<T> getEntitiesOfClass(@NotNull Class<? extends T> cs, @NotNull AxisAlignedBB aabb, @Nullable Predicate<? super T> predicate) {
        return MixinUtil.getEntitiesOfClass(cs, aabb, predicate, getProfiler(), getMaxEntityRadius(), this.getChunkSource());
    }
}

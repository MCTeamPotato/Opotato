package com.teampotato.opotato.mixin.opotato.minecraft.entity;

import com.teampotato.opotato.api.mutable.IAABB;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.CollisionSpliterator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(value = Entity.class, priority = 1)
public abstract class MixinEntity {
    @Mutable @Shadow @Final private Set<String> tags;

    @Shadow public abstract AABB getBoundingBox();

    @Shadow public Level level;

    @Shadow public boolean noPhysics;

    @Shadow private EntityDimensions dimensions;

    @Shadow public abstract double getX();

    @Shadow public abstract double getEyeY();

    @Shadow public abstract double getZ();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void optimizeTags(EntityType<?> arg, Level arg2, CallbackInfo ci) {
        this.tags = new ObjectOpenHashSet<>(this.tags);
    }

    @Redirect(method = "move", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;noneMatch(Ljava/util/function/Predicate;)Z", remap = false))
    private boolean onMove(Stream<BlockPos> instance, Predicate<BlockPos> predicate) {
        AABB box = this.getBoundingBox();
        for (BlockPos pos :
                BlockPos.betweenClosed(
                        Mth.floor(box.minX - 0.001),
                        Mth.floor(box.minY - 0.001),
                        Mth.floor(box.minZ - 0.001),
                        Mth.floor(box.maxX - 0.001),
                        Mth.floor(box.maxY - 0.001),
                        Mth.floor(box.maxZ - 0.001)
                )
        ) {
            BlockState state = this.level.getBlockState(pos);
            if (state.is(BlockTags.FIRE) || state.is(Blocks.LAVA) || state.isBurning(this.level, pos)) return true;
        }
        return false;
    }

    @Unique private static final Consumer<VoxelShape> emptyConsumer = voxelShape -> {};

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    public boolean isInWall() {
        if (this.noPhysics) {
            return false;
        } else {
            float f1 = this.dimensions.width * 0.8F;
            CollisionSpliterator voxelShapeIterator = new CollisionSpliterator(this.level, (Entity) (Object)this, ((IAABB)AABB.ofSize(f1, 0.10000000149011612, f1))._move(this.getX(), this.getEyeY(), this.getZ()), (blockState, blockPos) -> blockState.isSuffocating(this.level, blockPos));
            return voxelShapeIterator.tryAdvance(emptyConsumer);
        }
    }
}

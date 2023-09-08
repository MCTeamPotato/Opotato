package com.teampotato.opotato.mixin.opotato.minecraft.client;

import com.teampotato.opotato.api.mutable.IAABB;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.BubbleColumnAmbientSoundHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "unchecked"})
@Mixin(BubbleColumnAmbientSoundHandler.class)
public abstract class MixinBubbleColumnAmbientSoundHandler {
    @Shadow @Final private LocalPlayer player;

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/Optional;orElse(Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
    private <T> @Nullable T onTick(Optional<T> instance, T other) {
        AABB boundingBox = ((IAABB)this.player.getBoundingBox().inflate(0.0, -0.4000000059604645, 0.0))._deflate(0.001);
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();

        for (int x = Mth.floor(boundingBox.minX); x <= Mth.floor(boundingBox.maxX); ++x) {
            for (int y = Mth.floor(boundingBox.minY); y <= Mth.floor(boundingBox.maxY); ++y) {
                for (int z = Mth.floor(boundingBox.minZ); z <= Mth.floor(boundingBox.maxZ); ++z) {
                    blockPos.set(x, y, z);
                    BlockState blockState = this.player.level.getBlockState(blockPos);
                    if (blockState.is(Blocks.BUBBLE_COLUMN)) return (T) blockState;
                }
            }
        }

        return null;
    }
}

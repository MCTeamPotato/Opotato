package com.teampotato.opotato.mixin.opotato.minecraft.ai;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.InsideBrownianWalk;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Mixin(InsideBrownianWalk.class)
public abstract class MixinInsideBrownianWalk {
    @Shadow @Final private float speedModifier;

    /**
     * @author Kasualix
     * @reason avoid stream and allocation
     */
    @Overwrite
    protected void start(ServerLevel level, @NotNull PathfinderMob entity, long gameTime) {
        BlockPos blockPosition = entity.blockPosition();
        int x = blockPosition.getX();
        int y = blockPosition.getY();
        int z = blockPosition.getZ();
        List<BlockPos> blockPosList = Lists.newArrayList(BlockPos.betweenClosed(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1));

        Collections.shuffle(blockPosList, ThreadLocalRandom.current());

        Optional<BlockPos> optional = Optional.empty();

        for (BlockPos blockPos : blockPosList) {
            if (!level.canSeeSky(blockPos) && level.loadedAndEntityCanStandOn(blockPos, entity) && level.noCollision(entity)) {
                optional = Optional.of(blockPos);
                break;
            }
        }

        optional.ifPresent((blockPos) -> entity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(blockPos, this.speedModifier, 0)));
    }
}

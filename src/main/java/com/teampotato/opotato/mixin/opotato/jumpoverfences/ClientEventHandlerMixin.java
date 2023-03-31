package com.teampotato.opotato.mixin.opotato.jumpoverfences;

import com.kreezcraft.jumpoverfences.ClientEventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = ClientEventHandler.class, remap = false)
public class ClientEventHandlerMixin {
    /**
     * @author Doctor Who
     * @reason Saving The World
     */
    @Overwrite
    private static boolean isPlayerNextToFence(ClientPlayerEntity player) {
        BlockPos playerPos = player.blockPosition();
        Iterable<BlockPos> positions = BlockPos.betweenClosed(playerPos.offset(-1, 0, -1), playerPos.offset(1, 0, 1));
        for (BlockPos blockPos : positions) {
            if (blockPos.equals(playerPos)) continue;
            BlockState blockState = player.level.getBlockState(blockPos);
            if (blockState.getBlock() instanceof FenceBlock || blockState.getBlock() instanceof WallBlock) return true;
        }
        return false;
    }
}

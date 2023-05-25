package com.teampotato.opotato.mixin.opotato.jumpoverfences;

import com.kreezcraft.jumpoverfences.ClientEventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = ClientEventHandler.class, remap = false)
public abstract class ClientEventHandlerMixin {
    /**
     * @author Kasualix
     * @reason Optimize fence check
     */
    @Overwrite
    private static boolean isPlayerNextToFence(ClientPlayerEntity player) {
        BlockPos playerPos = player.blockPosition();
        for(BlockPos blockPos : BlockPos.betweenClosed(playerPos.offset(-1, 0, -1), playerPos.offset(1, 0, 1))) {
            Block block = player.level.getBlockState(blockPos).getBlock();
            if (block instanceof FenceBlock || block instanceof WallBlock) return true;
        }
        return false;
    }
}

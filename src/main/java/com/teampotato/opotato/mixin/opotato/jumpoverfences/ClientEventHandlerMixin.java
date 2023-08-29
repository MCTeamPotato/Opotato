package com.teampotato.opotato.mixin.opotato.jumpoverfences;

import com.kreezcraft.jumpoverfences.ClientEventHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.WallBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = ClientEventHandler.class, remap = false)
public abstract class ClientEventHandlerMixin {
    /**
     * @author Kasualix
     * @reason Optimize fence check
     */
    @Overwrite
    private static boolean isPlayerNextToFence(LocalPlayer player) {
        BlockPos playerPos = player.blockPosition();
        for (BlockPos blockPos : BlockPos.betweenClosed(playerPos.getX() - 1, playerPos.getY(), playerPos.getZ() - 1, playerPos.getX() + 1, playerPos.getY(), playerPos.getZ() + 1)) {
            Block block = player.level.getBlockState(blockPos).getBlock();
            if (block instanceof FenceBlock || block instanceof WallBlock) return true;
        }
        return false;
    }
}

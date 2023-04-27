package com.teampotato.opotato.mixin.opotato.jumpoverfences;

import com.kreezcraft.jumpoverfences.CommonClass;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.WallBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.stream.StreamSupport;

@Mixin(value = CommonClass.class, remap = false)
public class ClientEventHandlerMixin {
    /**
     * @author Kasualix
     * @reason Optimize fence check
     */
    @Overwrite
    private static boolean isPlayerNextToFence(LocalPlayer player) {
        BlockPos playerPos = player.blockPosition();
        return StreamSupport.stream(BlockPos.betweenClosed(playerPos.offset(-1, 0, -1), playerPos.offset(1, 0, 1)).spliterator(), false).anyMatch(blockPos -> {
            Block block = player.level.getBlockState(blockPos).getBlock();
            return (block instanceof FenceBlock || block instanceof WallBlock);
        });
    }
}

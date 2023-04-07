package com.teampotato.opotato.mixin.opotato.jumpoverfences;

import com.kreezcraft.jumpoverfences.ClientEventHandler;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.stream.StreamSupport;

@Mixin(value = ClientEventHandler.class, remap = false)
public class ClientEventHandlerMixin {
    /**
     * @author Doctor Who
     * @reason Saving The World
     */
    @Overwrite
    private static boolean isPlayerNextToFence(ClientPlayerEntity player) {
        BlockPos playerPos = player.blockPosition();
        return StreamSupport.stream(BlockPos.betweenClosed(playerPos.offset(-1, 0, -1), playerPos.offset(1, 0, 1)).spliterator(), false)
                .anyMatch(blockPos -> player.level.getBlockState(blockPos).getBlock() instanceof FenceBlock ||
                        player.level.getBlockState(blockPos).getBlock() instanceof WallBlock);
    }
}

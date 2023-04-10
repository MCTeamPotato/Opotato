package com.teampotato.opotato.mixin.opotato.jumpoverfences;

import com.kreezcraft.jumpoverfences.ClientEventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.StreamSupport;

@Mixin(value = ClientEventHandler.class, remap = false)
public class ClientEventHandlerMixin {
    @Inject(method = "isPlayerNextToFence", at = @At("HEAD"), cancellable = true)
    private static void optimizeCheck(ClientPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        BlockPos playerPos = player.blockPosition();
        cir.setReturnValue(StreamSupport.stream(BlockPos.betweenClosed(playerPos.offset(-1, 0, -1), playerPos.offset(1, 0, 1)).spliterator(), false).anyMatch(blockPos -> {
            Block block = player.level.getBlockState(blockPos).getBlock();
            return (block instanceof FenceBlock || block instanceof WallBlock);
        }));
        cir.cancel();
    }
}

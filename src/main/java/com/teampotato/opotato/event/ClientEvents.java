package com.teampotato.opotato.event;

import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "opotato", value = {Dist.CLIENT})
public class ClientEvents {

    public static boolean isPlayerNextToFence(LocalPlayer player) {
        double x = player.getX() - 1.0D;
        double y = player.getY();
        double z = player.getZ() - 1.0D;
        for(int i = 0; i < 9; ++i) {
            int row = i / 3;
            int col = i % 3;
            if (row == 1 && col == 1) continue;
            Block block = player.level.getBlockState( new BlockPos(x + (double)col, y, z + (double)row)).getBlock();
            if (block instanceof FenceBlock || block instanceof WallBlock) return true;
        }
        return false;
    }

    @SubscribeEvent
    public static void playerJumpFence(LivingEvent.LivingJumpEvent event) {
        if (!PotatoCommonConfig.ENABLE_FENCE_JUMP.get() || !(event.getEntity() instanceof LocalPlayer)) return;
        LocalPlayer player = (LocalPlayer) event.getEntity();
        if (isPlayerNextToFence(player) && player.input.jumping) {
            player.setDeltaMovement(player.getDeltaMovement().add(0.0D, 0.05D, 0.0D));
        }
    }
}

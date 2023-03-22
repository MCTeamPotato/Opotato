package com.teampotato.opotato.event;

import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = "opotato", value = {Dist.CLIENT})
public class ClientEvents {

    public static boolean isPlayerNextToFence(ClientPlayerEntity player) {
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
        if (!PotatoCommonConfig.ENABLE_FENCE_JUMP.get() || !(event.getEntity() instanceof ClientPlayerEntity)) return;
        ClientPlayerEntity player = (ClientPlayerEntity) event.getEntity();
        if (isPlayerNextToFence(player) && player.input.jumping) {
            player.setDeltaMovement(player.getDeltaMovement().add(0.0D, 0.05D, 0.0D));
        }
    }

    public static final KeyBinding noHurtCamKey = new KeyBinding("opotato.key.no_hurt_cam",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "opotato.key.no_hurt_cam.category");

    public static boolean toggledOn = false;

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(noHurtCamKey));
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        toggledOn = !toggledOn;
    }
}

package com.teampotato.opotato.mixin.opotato.blueskies;

import com.legacy.blue_skies.events.SkiesHooks;
import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SkiesHooks.class, remap = false)
public abstract class MixinSkiesHooks {
    @Inject(method = "breakSpeedHook", at = @At("HEAD"), cancellable = true)
    private static void configurableBreakHook(float speed, BlockState state, BlockPos pos, PlayerEntity player, CallbackInfoReturnable<Float> cir) {
        if (PotatoCommonConfig.ENABLE_BLUE_SKIES_NERF.get()) return;
        cir.setReturnValue(speed);
        cir.cancel();
    }
}

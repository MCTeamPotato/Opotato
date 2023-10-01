package com.teampotato.opotato.mixin.opotato.blueskies;

import com.legacy.blue_skies.events.SkiesHooks;
import com.teampotato.opotato.config.mods.BlueSkiesExtraConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SkiesHooks.class, remap = false)
public class MixinSkiesHooks {
    @Inject(method = "breakSpeedHook", at = @At("HEAD"), cancellable = true)
    private static void disableNerf(float speed, BlockState state, BlockPos pos, Player player, CallbackInfoReturnable<Float> cir) {
        if (BlueSkiesExtraConfig.enableDimensionalNerf.get()) return;
        cir.setReturnValue(speed);
    }
}

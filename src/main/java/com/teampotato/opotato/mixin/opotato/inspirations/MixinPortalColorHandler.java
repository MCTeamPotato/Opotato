package com.teampotato.opotato.mixin.opotato.inspirations;

import com.teampotato.opotato.Opotato;
import knightminer.inspirations.tweaks.client.PortalColorHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PortalColorHandler.class, remap = false)
public abstract class MixinPortalColorHandler {
    @Inject(method = "getColor", at = @At("HEAD"), cancellable = true)
    private void rbCompat(BlockState state, BlockAndTintGetter world, BlockPos pos, int tintValue, CallbackInfoReturnable<Integer> cir) {
        if (Opotato.isRubidiumLoaded) {
            cir.setReturnValue(-1);
            cir.cancel();
        }
    }
}

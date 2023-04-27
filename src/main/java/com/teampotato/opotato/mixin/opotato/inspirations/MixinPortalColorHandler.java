package com.teampotato.opotato.mixin.opotato.inspirations;

import knightminer.inspirations.tweaks.client.PortalColorHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PortalColorHandler.class, remap = false)
public class MixinPortalColorHandler {
    @Inject(method = "getColor", at = @At("HEAD"), cancellable = true)
    private void rbCompat(BlockState state, BlockAndTintGetter world, BlockPos pos, int tintValue, CallbackInfoReturnable<Integer> cir) {
        if (!ModList.get().isLoaded("rubidium")) return;
        cir.setReturnValue(-1);
        cir.cancel();
    }
}

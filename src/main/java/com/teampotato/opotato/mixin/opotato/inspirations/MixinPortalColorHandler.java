package com.teampotato.opotato.mixin.opotato.inspirations;

import knightminer.inspirations.tweaks.client.PortalColorHandler;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PortalColorHandler.class, remap = false)
public class MixinPortalColorHandler {
    @Inject(method = "getColor", at = @At("HEAD"), cancellable = true)
    private void rbCompat(BlockState state, IBlockDisplayReader world, BlockPos pos, int tintValue, CallbackInfoReturnable<Integer> cir) {
        if (!ModList.get().isLoaded("rubidium")) return;
        cir.setReturnValue(-1);
        cir.cancel();
    }
}
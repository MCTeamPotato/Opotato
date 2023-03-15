package com.teampotato.opotato.mixin.fastchest;

import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.EnderChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TrappedChestTileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TileEntityRendererDispatcher.class)
public class BlockEntityRenderDispatcherMixin {
    @Inject(method = "getRenderer", at = @At("HEAD"), cancellable = true)
    private <E extends TileEntity> void fastchest_get(E blockEntity, CallbackInfoReturnable<TileEntityRenderer<E>> cir) {
        Class<?> beClass = blockEntity.getClass();
        if (
                beClass == ChestTileEntity.class ||
                beClass == TrappedChestTileEntity.class ||
                beClass == EnderChestTileEntity.class
        ) {
            cir.setReturnValue(null);
        }
    }
}

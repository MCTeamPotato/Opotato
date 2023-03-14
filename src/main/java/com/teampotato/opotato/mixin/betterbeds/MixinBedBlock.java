package com.teampotato.opotato.mixin.betterbeds;

import net.minecraft.block.*;
import net.minecraft.util.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BedBlock.class, priority = 2000)
public abstract class MixinBedBlock extends HorizontalBlock {
    protected MixinBedBlock(AbstractBlock.Properties settings) {
        super(settings);
    }

    @Inject(at = @At("RETURN"), method = "getRenderShape", cancellable = true)
    private void getRenderShape(BlockState state, CallbackInfoReturnable<BlockRenderType> cir) {
        cir.setReturnValue(BlockRenderType.MODEL);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean skipRendering(BlockState state, BlockState neighborState, Direction offset) {
        return neighborState.getBlock() instanceof BedBlock;
    }
}

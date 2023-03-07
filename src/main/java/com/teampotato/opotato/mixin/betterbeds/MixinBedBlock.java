package com.teampotato.opotato.mixin.betterbeds;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BedBlock.class, priority = 2000)
public abstract class MixinBedBlock extends HorizontalDirectionalBlock {
    protected MixinBedBlock(Properties settings) {
        super(settings);
    }

    @Inject(at = @At("RETURN"), method = "getRenderType", cancellable = true)
    private void getRenderType(BlockState state, CallbackInfoReturnable<RenderShape> cir) {
        cir.setReturnValue(RenderShape.MODEL);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean skipRendering(BlockState state, BlockState neighborState, Direction offset) {
        return neighborState.getBlock() instanceof BedBlock;
    }
}

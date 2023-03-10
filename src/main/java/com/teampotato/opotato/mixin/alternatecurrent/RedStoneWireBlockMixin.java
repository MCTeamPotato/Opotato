package com.teampotato.opotato.mixin.alternatecurrent;

import com.teampotato.opotato.interfaces.alternatecurrent.IServerWorld;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RedstoneWireBlock.class)
public class RedStoneWireBlockMixin {

	@Inject(
		method = "updatePowerStrength",
		cancellable = true,
		at = @At(
			value = "HEAD"
		)
	)
	private void onUpdate(World level, BlockPos pos, BlockState state, CallbackInfo ci) {
		ci.cancel();
	}

	@Inject(
		method = "onPlace",
		at = @At(
			value = "INVOKE",
			shift = Shift.BEFORE,
			target = "Lnet/minecraft/block/RedstoneWireBlock;updatePowerStrength(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"
		)
	)
	private void onPlace(BlockState state, World level, BlockPos pos, BlockState oldState, boolean moved, CallbackInfo ci) {
		((IServerWorld)level).getWireHandler().onWireAdded(pos);
	}

	@Inject(
		method = "onRemove",
		at = @At(
			value = "INVOKE",
			shift = Shift.BEFORE,
				target = "Lnet/minecraft/block/RedstoneWireBlock;updatePowerStrength(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"
		)
	)
	private void onRemove(BlockState state, World level, BlockPos pos, BlockState newState, boolean moved, CallbackInfo ci) {
		((IServerWorld)level).getWireHandler().onWireRemoved(pos, state);
	}

	@Inject(
		method = "neighborChanged",
		cancellable = true,
		at = @At(
			value = "HEAD"
		)
	)
	private void onNeighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos fromPos, boolean notify, CallbackInfo ci) {
		((IServerWorld)level).getWireHandler().onWireUpdated(pos);
		ci.cancel();
	}
}

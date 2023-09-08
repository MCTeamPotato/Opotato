package com.teampotato.opotato.mixin.alternatecurrent;

import com.teampotato.opotato.api.IServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RedStoneWireBlock.class)
public abstract class RedstoneWireBlockMixin {

	@Inject(method = "updatePowerStrength", cancellable = true, at = @At(value = "HEAD"))
	private void onUpdate(Level level, BlockPos pos, BlockState state, @NotNull CallbackInfo ci) {
		// Using redirects for calls to this method makes conflicts with
		// other mods more likely, so we inject-cancel instead.
		ci.cancel();

	}

	@Inject(method = "onPlace", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/RedStoneWireBlock;updatePowerStrength(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"))
	private void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston, CallbackInfo ci) {
		((IServerLevel)level)._getWireHandler().onWireAdded(pos);
	}

	@Inject(method = "onRemove", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/RedStoneWireBlock;updatePowerStrength(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"))
	private void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston, CallbackInfo ci) {
		((IServerLevel)level)._getWireHandler().onWireRemoved(pos, state);
	}

	@Inject(method = "neighborChanged", cancellable = true, at = @At(value = "HEAD"))
	private void onNeighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston, CallbackInfo ci) {
		if (((IServerLevel)level)._getWireHandler().onWireUpdated(pos)) ci.cancel(); // needed to fix duplication bugs
	}
}

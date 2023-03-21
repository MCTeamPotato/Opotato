package com.teampotato.opotato.util.ac.wire;

import com.teampotato.opotato.util.ac.Redstone;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

/**
 * A WireNode is a Node that represents a wire in the world. It stores all the
 * information about the wire that the WireHandler needs to calculate power
 * changes.
 * 
 * @author Space Walker
 */
public class WireNode extends Node {

	final WireConnectionManager connections;

	/** The power level this wire currently holds in the world. */
	int currentPower;
	/**
	 * While calculating power changes for a network, this field is used to keep
	 * track of the power level this wire should have.
	 */
	int virtualPower;
	/** The power level received from non-wire components. */
	int externalPower;
	/**
	 * A 4-bit number that keeps track of the power flow of the wires that give this
	 * wire its power level.
	 */
	int flowIn;
	/** The direction of power flow, based on the incoming flow. */
	int iFlowDir;
	boolean added;
	boolean removed;
	boolean shouldBreak;
	boolean root;
	boolean discovered;
	boolean searched;

	/** The next wire in the simple queue. */
	WireNode next_wire;

	WireNode(ServerWorld level, BlockPos pos, BlockState state) {
		super(level);

		this.pos = pos.immutable();
		this.state = state;

		this.connections = new WireConnectionManager(this);

		this.virtualPower = this.currentPower = this.state.getValue(RedstoneWireBlock.POWER);
		this.priority = priority();
	}

	@Override
	Node set(BlockPos pos, BlockState state, boolean clearNeighbors) {
		throw new UnsupportedOperationException("Cannot update a WireNode!");
	}

	@Override
	int priority() {
		return MathHelper.clamp(virtualPower, Redstone.SIGNAL_MIN, Redstone.SIGNAL_MAX);
	}

	@Override
	public boolean isWire() {
		return true;
	}

	@Override
	public WireNode asWire() {
		return this;
	}

	boolean offerPower(int power, int iDir) {
		if (removed || shouldBreak) {
			return false;
		}
		if (power == virtualPower) {
			flowIn |= (1 << iDir);
			return false;
		}
		if (power > virtualPower) {
			virtualPower = power;
			flowIn = (1 << iDir);

			return true;
		}

		return false;
	}

	boolean setPower() {
		if (removed) {
			return true;
		}

		state = level.getBlockState(pos);

		if (!state.is(Blocks.REDSTONE_WIRE)) {
			return false; // we should never get here
		}

		if (shouldBreak) {
			Block.dropResources(state, level, pos);
			level.setBlock(pos, Blocks.AIR.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);

			return true;
		}

		currentPower = MathHelper.clamp(virtualPower, Redstone.SIGNAL_MIN, Redstone.SIGNAL_MAX);
		state = state.setValue(RedstoneWireBlock.POWER, currentPower);

		return LevelHelper.setWireState(level, pos, state, added);
	}
}

package com.teampotato.opotato.mixin.alternatecurrent;

import com.teampotato.opotato.interfaces.IServerWorld;
import com.teampotato.opotato.util.alternatecurrent.wire.WireHandler;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerWorld.class)
public class ServerWorldMixin implements IServerWorld {

	private final WireHandler wireHandler = new WireHandler((ServerWorld)(Object)this);

	@Override
	public WireHandler getWireHandler() {
		return wireHandler;
	}
}

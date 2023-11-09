package com.teampotato.opotato.mixin.api;

import com.teampotato.opotato.api.IServerLevel;
import com.teampotato.opotato.util.alternatecurrent.wire.WireHandler;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerLevel.class)
public class ServerLevelMixin implements IServerLevel {

	@Unique
	private final WireHandler opotato$wireHandler = new WireHandler((ServerLevel)(Object)this);

	@Override
	public WireHandler potato$getWireHandler() {
		return opotato$wireHandler;
	}
}

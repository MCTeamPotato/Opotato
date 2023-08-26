package com.teampotato.opotato.mixin.alternatecurrent;

import org.spongepowered.asm.mixin.Mixin;

import com.teampotato.opotato.api.IServerLevel;
import com.teampotato.opotato.wire.WireHandler;

import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerLevel.class)
public class ServerLevelMixin implements IServerLevel {

	@Unique
	private final WireHandler opotato$wireHandler = new WireHandler((ServerLevel)(Object)this);

	@Override
	public WireHandler opotato$getWireHandler() {
		return opotato$wireHandler;
	}
}

package com.teampotato.potatoptimize.mixin.alternatecurrent;

import com.teampotato.potatoptimize.access.IServerLevel;
import com.teampotato.potatoptimize.wire.WireHandler;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerLevel.class)
public class ServerLevelMixin implements IServerLevel {

    private final WireHandler wireHandler = new WireHandler((ServerLevel)(Object)this);

    @Override
    public WireHandler getWireHandler() {
        return wireHandler;
    }
}

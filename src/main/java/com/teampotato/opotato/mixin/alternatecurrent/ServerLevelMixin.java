package com.teampotato.opotato.mixin.alternatecurrent;

import com.teampotato.opotato.util.ac.access.IServerLevel;
import com.teampotato.opotato.util.ac.wire.WireHandler;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerWorld.class)
public class ServerLevelMixin implements IServerLevel {

    private final WireHandler wireHandler = new WireHandler((ServerWorld)(Object)this);

    @Override
    public WireHandler getWireHandler() {
        return wireHandler;
    }
}

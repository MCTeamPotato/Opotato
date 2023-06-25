package com.teampotato.opotato.mixin.api;

import com.teampotato.opotato.api.ExtendedServerWorld;
import net.minecraft.entity.Entity;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(ServerWorld.class)
public abstract class ServerWorldImpl implements ExtendedServerWorld {
    @Shadow @Nullable protected abstract Entity findAddedOrPendingEntity(UUID uuid);

    @Override
    public Entity findAddedOrPendingEntityPublic(UUID uuid) {
        return this.findAddedOrPendingEntity(uuid);
    }
}

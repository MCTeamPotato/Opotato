package com.teampotato.opotato.mixin.stxck.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.teampotato.opotato.util.stxck.StxckUtil.*;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {

    @Shadow public abstract ClientLevel getLevel();

    @Redirect(
            method = "handleTakeItemEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/ClientLevel;removeEntity(I)V", ordinal = 0
            )
    )
    private void handleRemoveItemEntity(ClientLevel instance, int entityId) {
        Entity entity = this.getLevel().getEntity(entityId);
        if (entity == null || tryRefillItemStackOnEntityRemove(entity)) return;
        this.getLevel().removeEntity(entityId);
    }
}

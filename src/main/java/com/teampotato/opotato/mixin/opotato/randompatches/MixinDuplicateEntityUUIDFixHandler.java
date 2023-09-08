package com.teampotato.opotato.mixin.opotato.randompatches;

import com.therandomlabs.randompatches.world.DuplicateEntityUUIDFixHandler;
import net.minecraftforge.event.world.ChunkEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DuplicateEntityUUIDFixHandler.class, remap = false)
public abstract class MixinDuplicateEntityUUIDFixHandler {
    @Inject(method = "onChunkLoad", at = @At("HEAD"), cancellable = true)
    private static void noFix(ChunkEvent.Load event, @NotNull CallbackInfo ci) {
        ci.cancel();
    }
}

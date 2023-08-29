package com.teampotato.opotato.mixin.opotato.minecraft;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.storage.IOWorker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.Map;

@Mixin(IOWorker.class)
public abstract class MixinIOWorker {
    @Mutable @Shadow @Final private Map<ChunkPos, IOWorker.PendingStore> pendingWrites;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(File file, boolean bl, String string, CallbackInfo ci) {
        this.pendingWrites = new Object2ObjectLinkedOpenHashMap<>(this.pendingWrites);
    }
}

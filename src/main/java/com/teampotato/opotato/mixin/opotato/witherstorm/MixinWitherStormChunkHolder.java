package com.teampotato.opotato.mixin.opotato.witherstorm;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.level.ChunkPos;
import nonamecrackers2.witherstormmod.common.capability.WitherStormChunkHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = WitherStormChunkHolder.class, remap = false)
public abstract class MixinWitherStormChunkHolder {

    @Shadow private List<ChunkPos> allLoadedChunks;

    @Inject(method = {"<init>()V", "<init>(Lnet/minecraft/server/level/ServerLevel;)V"}, at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        this.allLoadedChunks= new ObjectArrayList<>(this.allLoadedChunks);
    }
}

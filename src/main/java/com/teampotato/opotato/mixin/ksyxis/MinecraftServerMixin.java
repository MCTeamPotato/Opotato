package com.teampotato.opotato.mixin.ksyxis;

import com.teampotato.opotato.Opotato;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Redirect(method = "prepareLevels", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerChunkCache;getTickingGenerated()I"))
    public int prepareLevels_Redirect(ServerChunkCache serverChunkCache) {
        Opotato.LOGGER.info("Hi there, Opotato has removed the 441 chunk loading.");
        Opotato.LOGGER.info("If you encounter loading stuck at 0% or 100%, please try to write 'mixin.ksyxis=false' in config/opotato-mixins.properties.");
        return serverChunkCache.getLoadedChunksCount();
    }
}
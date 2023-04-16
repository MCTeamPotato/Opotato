package com.teampotato.opotato.mixin.ksyxis;

import com.teampotato.opotato.Opotato;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerChunkProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = MinecraftServer.class, priority = 900)
public class MinecraftServerMixin {
    @Redirect(method = "prepareLevels", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/server/ServerChunkProvider;getTickingGenerated()I"))
    private int prepareLevels_Redirect(ServerChunkProvider provider) {
        Opotato.LOGGER.error("Hi there, Opotato has removed the 441 chunk loading.");
        Opotato.LOGGER.error("If you encounter loading stuck at 0% or 100%, please try to write 'mixin.ksyxis=false' in config/opotato-mixins.properties.");
        Opotato.LOGGER.error("This is not an error but a warn exactly. lol");
        return 441;
    }
}
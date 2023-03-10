package com.teampotato.opotato.mixin.nec.client;

import net.minecraft.CrashReport;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.File;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServerClientOnly {
    @Redirect(method = "runServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/CrashReport;saveToFile(Ljava/io/File;)Z"))
    private boolean disableIntegratedServerWriteToFileOnCrash(CrashReport instance, File file) {
        return true;
    }
}

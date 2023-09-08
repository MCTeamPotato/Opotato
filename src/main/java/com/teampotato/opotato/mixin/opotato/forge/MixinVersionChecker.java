package com.teampotato.opotato.mixin.opotato.forge;

import net.minecraftforge.fml.VersionChecker;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = VersionChecker.class, remap = false)
public abstract class MixinVersionChecker {
    @Inject(method = "startVersionCheck", at = @At("HEAD"), cancellable = true)
    private static void onConnect(@NotNull CallbackInfo ci) {
        ci.cancel();
    }
}

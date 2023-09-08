package com.teampotato.opotato.mixin.smoothmenu;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("SpellCheckingInspection")
@Mixin(Minecraft.class)
public abstract class SmoothMenuMixin {
    @Shadow @Final private Window window;

    @Inject(method = "getFramerateLimit", at = @At("HEAD"), cancellable = true)
    private void onGetFramerateLimit(@NotNull CallbackInfoReturnable<Integer> ci) {
        ci.setReturnValue(this.window.getFramerateLimit());
        ci.cancel();
    }
}

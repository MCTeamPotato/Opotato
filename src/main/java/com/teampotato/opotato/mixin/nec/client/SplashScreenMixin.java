package com.teampotato.opotato.mixin.nec.client;

import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LoadingOverlay.class)
public interface SplashScreenMixin {
    @Accessor("MOJANG_STUDIOS_LOGO_LOCATION")
    static void setLogo(ResourceLocation logo) {
        throw new UnsupportedOperationException();
    }
}

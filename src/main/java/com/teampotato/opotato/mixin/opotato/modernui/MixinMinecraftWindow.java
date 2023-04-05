package com.teampotato.opotato.mixin.opotato.modernui;

import net.minecraft.client.MainWindow;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MainWindow.class)
public class MixinMinecraftWindow {
    @Shadow
    private double guiScale;

    @Inject(
            method = {"setGuiScale"},
            at = {@At("HEAD")}
    )
    private void onSetGuiScale(double scaleFactor, CallbackInfo ci) {
        if (ModList.get().isLoaded("modernui") && ModList.get().isLoaded("rubidium")) {
            int oldScale = (int) this.guiScale;
            int newScale = (int) scaleFactor;
            if ((double) newScale != scaleFactor) {
                icyllis.modernui.ModernUI.LOGGER.warn(icyllis.modernui.ModernUI.MARKER, "Gui scale should be an integer: {}", scaleFactor);
            }

            icyllis.modernui.view.ViewConfig.get().setViewScale((float) newScale * 0.5F);
            if (icyllis.modernui.platform.RenderCore.isInitialized() && oldScale != newScale) {
                icyllis.modernui.textmc.TextLayoutEngine.getInstance().reload();
            }
        }
    }
}

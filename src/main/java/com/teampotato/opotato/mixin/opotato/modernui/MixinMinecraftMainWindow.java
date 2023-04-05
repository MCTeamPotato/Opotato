package com.teampotato.opotato.mixin.opotato.modernui;

import net.minecraft.client.MainWindow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MainWindow.class)
public class MixinMinecraftMainWindow {
    @Shadow
    private int framebufferWidth;

    @Shadow
    private int framebufferHeight;

    /**
     * @author Doctor Who
     * @reason Saving The World
     */
    @Overwrite
    public int calculateScale(int p_216521_1_, boolean p_216521_2_) {
        int i;
        for(i = 1; i != p_216521_1_ && i < this.framebufferWidth && i < this.framebufferHeight && this.framebufferWidth / (i + 1) >= 320 && this.framebufferHeight / (i + 1) >= 240; ++i) {

        }

        if (p_216521_2_ && i % 2 != 0) ++i;

        return i;
    }
}

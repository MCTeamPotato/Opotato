package com.teampotato.opotato.mixin.opotato.modernui;

import com.teampotato.opotato.util.MixinUtil;
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
        return MixinUtil.calculateScale(p_216521_1_, p_216521_2_, this.framebufferWidth, this.framebufferHeight);
    }
}

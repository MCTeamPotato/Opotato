package com.teampotato.opotato.mixin.opotato.modernui;

import net.minecraft.client.MainWindow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MainWindow.class)
public abstract class MixinMinecraftMainWindow {
    @Shadow
    private int framebufferWidth;

    @Shadow
    private int framebufferHeight;

    /**
     * @author
     * Kasualix
     *
     * @reason
     * If you install Modern UI with Rubidium, then your GUI Scale will always be 1 less than normal because Modern UI change the way MC calculate GUI Scale by overwriting this method.
     * This will cause huge inconvenience on some computers.
     * This mixin revert the calculateScale method to the vanilla to fix this problem
     * Opotato's mixin has higher execution priority then Modern UI, so Modern UI's overwrite will be skipped when you launch your game.
     */
    @Overwrite
    public int calculateScale(int p_216521_1_, boolean p_216521_2_) {
        int i;
        for(i = 1; i != p_216521_1_ && i < framebufferWidth && i < framebufferHeight && framebufferWidth / (i + 1) >= 320 && framebufferHeight / (i + 1) >= 240; ++i) {

        }

        if (p_216521_2_ && i % 2 != 0) ++i;

        return i;
    }
}

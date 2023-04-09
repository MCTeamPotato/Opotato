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
     * @author
     * Kasualix
     *
     * @reason
     * If you install Modern UI with Rubidium, then your GUI Scale will always be 1 less than normal because Modern UI change the way MC calculate GUI Scale by overwrite this method.
     * This will cause huge inconvenience in some computers.
     * This mixin revert the calculateScale method to the vanilla to fix this problem
     * Opotato's mixin has higher execution priority then Modern UI, so Modern UI's overwrite will be skipped when you launch your game.
     */
    @Overwrite
    public int calculateScale(int p_216521_1_, boolean p_216521_2_) {
        return MixinUtil.calculateScale(p_216521_1_, p_216521_2_, this.framebufferWidth, this.framebufferHeight);
    }
}

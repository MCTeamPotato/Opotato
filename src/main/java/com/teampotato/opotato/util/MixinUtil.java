package com.teampotato.opotato.util;

public class MixinUtil {
    public static int calculateScale(int p_216521_1_, boolean p_216521_2_, int framebufferWidth, int framebufferHeight) {
        int i;
        for(i = 1; i != p_216521_1_ && i < framebufferWidth && i < framebufferHeight && framebufferWidth / (i + 1) >= 320 && framebufferHeight / (i + 1) >= 240; ++i) {

        }

        if (p_216521_2_ && i % 2 != 0) ++i;

        return i;
    }
}

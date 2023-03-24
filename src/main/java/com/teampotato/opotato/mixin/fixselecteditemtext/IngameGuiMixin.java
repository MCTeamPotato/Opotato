package com.teampotato.opotato.mixin.fixselecteditemtext;

import net.minecraft.client.gui.IngameGui;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(IngameGui.class)
public class IngameGuiMixin {
    @Shadow
    protected int screenHeight;

    @ModifyVariable(method = "renderSelectedItemName",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerController;canHurtPlayer()Z")
            ,ordinal = 1)
    private int fixOffset(int old) {
        return fix(old, screenHeight);
    }

    private static int fix(int old, int scaledHeight) {
        int leftHeight = ForgeIngameGui.left_height;
        int rightHeight = ForgeIngameGui.right_height;
        int offsetHeight = Math.max(leftHeight,rightHeight);
        if (offsetHeight > 59) {
            return scaledHeight - offsetHeight;
        }
        return old;
    }
}

package com.teampotato.opotato.mixin.opotato.modernui;

import icyllis.modernui.forge.MixinConfigPlugin;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MixinConfigPlugin.class, remap = false)
public class MixinMUIConfigPlugin {
    @Inject(method = "shouldApplyMixin", at = @At("HEAD"), cancellable = true)
    private void rbCompat(String targetClassName, String mixinClassName, CallbackInfoReturnable<Boolean> cir) {
        if (!ModList.get().isLoaded("rubidium") || !mixinClassName.equals("icyllis.modernui.mixin.MixinWindow")) return;
        cir.setReturnValue(false);
    }
}

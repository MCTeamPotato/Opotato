package com.teampotato.opotato.mixin.opotato.quark;

import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.quark.base.module.QuarkModule;

@Mixin(value = QuarkModule.class, remap = false)
public abstract class MixinQuarkModule {
    @Redirect(method = "setEnabled", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V"))
    private void onLogInfo(Logger instance, String s) {}
}

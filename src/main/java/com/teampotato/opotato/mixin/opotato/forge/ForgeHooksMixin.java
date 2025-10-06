package com.teampotato.opotato.mixin.opotato.forge;

import net.minecraftforge.common.ForgeHooks;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ForgeHooks.class)
public abstract class ForgeHooksMixin {
    @Redirect(method = "putAttributesOld", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V", remap = false))
    private static void noLog(Logger instance, String s, Object object) {}
}

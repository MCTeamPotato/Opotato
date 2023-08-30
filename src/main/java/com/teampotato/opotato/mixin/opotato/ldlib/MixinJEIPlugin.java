package com.teampotato.opotato.mixin.opotato.ldlib;

import com.lowdragmc.lowdraglib.LDLMod;
import com.lowdragmc.lowdraglib.jei.JEIPlugin;
import mezz.jei.Internal;
import mezz.jei.api.runtime.IJeiRuntime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(value = JEIPlugin.class, remap = false)
public abstract class MixinJEIPlugin {
    @Shadow public static void setupInputHandler() {}

    @Redirect(method = "setupInputHandler", at = @At(value = "INVOKE", target = "Ljava/lang/reflect/Field;get(Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
    private static Object onGetHandler(Field field, Object o) {
        try {
            return field.get(Internal.class.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            LDLMod.LOGGER.info(e.getMessage());
            return null;
        }
    }

    @Inject(method = "onRuntimeAvailable", at = @At("TAIL"))
    private void setupInputHandlerOnRuntimeAvailable(IJeiRuntime jeiRuntime, CallbackInfo ci) {
        setupInputHandler();
    }
}
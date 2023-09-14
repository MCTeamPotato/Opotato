package com.teampotato.opotato.mixin.opotato.ldlib;

import com.lowdragmc.lowdraglib.jei.JEIPlugin;
import mezz.jei.Internal;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

@Mixin(value = JEIPlugin.class, remap = false)
public abstract class MixinJEIPlugin {
    @Redirect(method = "setupInputHandler", at = @At(value = "INVOKE", target = "Ljava/lang/reflect/Field;get(Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
    private static @Nullable Object onGetHandler(Field field, Object o) {
        try {
            Constructor<Internal> internalConstructor = Internal.class.getDeclaredConstructor();
            internalConstructor.setAccessible(true);
            return field.get(internalConstructor.newInstance());
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
    }
}
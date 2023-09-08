package com.teampotato.opotato.mixin.opotato.quark;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.quark.content.client.module.ImprovedTooltipsModule;

@Mixin(value = ImprovedTooltipsModule.class, remap = false)
public abstract class MixinImprovedTooltipsModule {
    @Inject(method = "ignore", at = @At("HEAD"), cancellable = true)
    private static void applyModCompatibility(@NotNull ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        ResourceLocation regName = stack.getItem().getRegistryName();
        if (regName != null) {
            String mod = regName.getNamespace();
            if (mod.equals("apotheosis") || mod.equals("immersive_armors")) {
                cir.setReturnValue(true);
                cir.cancel();
            }
        } else {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}

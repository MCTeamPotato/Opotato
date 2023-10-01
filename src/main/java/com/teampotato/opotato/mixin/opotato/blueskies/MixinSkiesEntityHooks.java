package com.teampotato.opotato.mixin.opotato.blueskies;

import com.legacy.blue_skies.entities.util.SkiesEntityHooks;
import com.teampotato.opotato.config.mods.BlueSkiesExtraConfig;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SkiesEntityHooks.class, remap = false)
public class MixinSkiesEntityHooks {
    @Inject(method = "nerfDamage", at = @At("HEAD"), cancellable = true)
    private static void disableNerf(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (BlueSkiesExtraConfig.enableDimensionalNerf.get()) return;
        cir.setReturnValue(amount);
    }

    @Inject(method = "nerfIndirectDamage", at = @At("HEAD"), cancellable = true)
    private static void disableIndirectNerf(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (BlueSkiesExtraConfig.enableDimensionalNerf.get()) return;
        cir.setReturnValue(Math.min(5.0F, amount));
    }
}

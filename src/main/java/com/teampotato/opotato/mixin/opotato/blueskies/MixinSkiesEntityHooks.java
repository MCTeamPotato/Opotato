package com.teampotato.opotato.mixin.opotato.blueskies;

import com.legacy.blue_skies.entities.util.SkiesEntityHooks;
import com.teampotato.opotato.config.mods.BlueSkiesExtraConfig;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SkiesEntityHooks.class, remap = false)
public class MixinSkiesEntityHooks {
    @Inject(method = "nerfDamage", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private static void enableEnhancedNerf(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (cir.getReturnValue() == amount * 0.3F && BlueSkiesExtraConfig.enableEnhancedDimensionalNerf.get()) cir.setReturnValue(0.0F);
    }

    @ModifyConstant(method = {"nerfIndirectDamage" , "lambda$nerfIndirectDamage$1"}, constant = @Constant(floatValue = 5.0F))
    private static float enableEnhancedNerf(float constant) {
        return BlueSkiesExtraConfig.enableEnhancedDimensionalNerf.get() ? 0.0F : constant;
    }
}

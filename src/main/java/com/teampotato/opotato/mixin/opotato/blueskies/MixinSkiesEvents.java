package com.teampotato.opotato.mixin.opotato.blueskies;

import com.legacy.blue_skies.events.SkiesEvents;
import com.teampotato.opotato.config.mods.BlueSkiesExtraConfig;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SkiesEvents.class, remap = false)
public abstract class MixinSkiesEvents {
    @Inject(method = "onLivingHurt", at = @At("HEAD"), cancellable = true)
    private static void makeNerfConfigurable(LivingDamageEvent event, CallbackInfo ci) {
        if (!BlueSkiesExtraConfig.enableDimensionalNerf.get()) ci.cancel();
    }

    @ModifyConstant(method = "onLivingHurt", constant = @Constant(floatValue = 5.0F))
    private static float enableEnhancedNerf(float constant) {
        return BlueSkiesExtraConfig.enableEnhancedDimensionalNerf.get() ? 0.0F : constant;
    }
}

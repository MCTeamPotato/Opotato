package com.teampotato.opotato.mixin.opotato.flowingagony;

import com.teampotato.opotato.config.mods.FlowingAgonyExtraConfig;
import love.marblegate.flowingagony.registry.EffectRegistry;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = EffectRegistry.class, remap = false)
public abstract class MixinEffectRegistry {
    @Dynamic
    @ModifyConstant(method = "lambda$static$6", constant = @Constant(doubleValue = 1.0))
    private static double implConfig(double constant) {
        return FlowingAgonyExtraConfig.prototypeChaoticPerHealthBonus.get().doubleValue();
    }
}

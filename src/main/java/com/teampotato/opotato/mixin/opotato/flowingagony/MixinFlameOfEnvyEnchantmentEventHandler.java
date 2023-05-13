package com.teampotato.opotato.mixin.opotato.flowingagony;

import love.marblegate.flowingagony.eventhandler.enchantment.FlameOfEnvyEnchantmentEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static com.teampotato.opotato.config.PotatoCommonConfig.ENVIOUS_KIND_DIFF_HEALTH;
import static com.teampotato.opotato.config.PotatoCommonConfig.ENVIOUS_KIND_EFFECT_DURATION;

@Mixin(value = FlameOfEnvyEnchantmentEventHandler.class, remap = false)
public abstract class MixinFlameOfEnvyEnchantmentEventHandler {
    @ModifyConstant(method = "doEnviousKindEnchantmentEvent", constant = @Constant(doubleValue = 10.0))
    private static double enviousKindDiffCheck(double constant) {
        return ENVIOUS_KIND_DIFF_HEALTH.get();
    }

    @ModifyConstant(method = "doEnviousKindEnchantmentEvent", constant = @Constant(intValue = 200))
    private static int enviousKindEffectDuration(int constant) {
        return ENVIOUS_KIND_EFFECT_DURATION.get();
    }
}

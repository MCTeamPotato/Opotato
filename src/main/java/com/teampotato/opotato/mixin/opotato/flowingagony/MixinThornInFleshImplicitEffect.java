package com.teampotato.opotato.mixin.opotato.flowingagony;

import love.marblegate.flowingagony.effect.special.ThornInFleshImplicitEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static com.teampotato.opotato.config.PotatoCommonConfig.*;

@Mixin(value = ThornInFleshImplicitEffect.class, remap = false)
public abstract class MixinThornInFleshImplicitEffect {
    @ModifyConstant(method = "applyEffectTick", constant = @Constant(floatValue = 1.0F))
    private static float onPlayerDamage(float constant) {
        return THORN_IN_FLESH_DAMAGE_ON_PLAYER.get();
    }

    @ModifyConstant(method = "applyEffectTick", constant = @Constant(intValue = 60))
    private static int onCalculateDuration(int constant) {
        return THORN_IN_FLESH_EFFECT_INTERVAL.get() + 10;
    }
}

package com.teampotato.opotato.mixin.opotato.flowingagony;

import love.marblegate.flowingagony.effect.special.EyesoreImplicitEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static com.teampotato.opotato.config.PotatoCommonConfig.*;

@Mixin(EyesoreImplicitEffect.class)
public abstract class MixinEyesoreImplicitEffect {
    @ModifyConstant(method = "applyEffectTick", constant = @Constant(intValue = 3))
    private static int onHurt(int constant) {
        return EYESORE_ARROW_EXPLOSION_DAMAGE.get();
    }

    @ModifyConstant(method = "applyEffectTick", constant = @Constant(intValue = 60, ordinal = 0))
    private static int onGiveBlindness(int constant) {
        return EYESORE_ARROW_BLINDNESS_DURATION.get();
    }

    @ModifyConstant(method = "applyEffectTick", constant = @Constant(intValue = 60, ordinal = 1))
    private static int onGiveSlowness(int constant) {
        return EYESORE_ARROW_SLOWNESS_DURATION.get();
    }
}

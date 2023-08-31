package com.teampotato.opotato.mixin.opotato.flowingagony;

import com.teampotato.opotato.config.mods.FlowingAgonyExtraConfig;
import love.marblegate.flowingagony.effect.LightburnFungalInfectionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LightburnFungalInfectionEffect.class)
public abstract class MixinLightburnFungalInfectionEffect {
    @ModifyConstant(method = "isDurationEffectTick", constant = @Constant(intValue = 40))
    private int onCheckTick(int constant) {
        return FlowingAgonyExtraConfig.lighthurnFungalInfectionHurtInterval.get();
    }

    @ModifyConstant(method = "applyEffectTick", constant = @Constant(floatValue = 3.0F))
    private float onTick(float constant) {
        return FlowingAgonyExtraConfig.lighthurnFungalInfectionHurtDamageAmounts.get().floatValue();
    }
}

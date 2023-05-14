package com.teampotato.opotato.mixin.opotato.flowingagony;

import love.marblegate.flowingagony.effect.special.ThornInFleshImplicitEffect;
import love.marblegate.flowingagony.eventhandler.enchantment.FlameOfEnvyEnchantmentEventHandler;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static com.teampotato.opotato.config.PotatoCommonConfig.*;

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

    @ModifyConstant(method = "doEyesoreEnchantmentEvent", constant = @Constant(intValue = 61))
    private static int onEyesoreGive(int constant) {
        return EYESORE_ARROW_EXPLODE_TICKS.get();
    }
}

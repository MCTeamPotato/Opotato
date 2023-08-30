package com.teampotato.opotato.mixin.opotato.flowingagony.themistakens;

import com.teampotato.opotato.config.mods.FlowingAgonyExtraConfig;
import love.marblegate.flowingagony.eventhandler.enchantment.TheMistakensEnchantmentEventHandler;
import love.marblegate.flowingagony.registry.EffectRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(value = TheMistakensEnchantmentEventHandler.class, remap = false)
public abstract class MixinTheMistakensEnchantmentEventHandler {
    @Inject(method = "doCorruptedKindredEnchantmentEvent", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/entity/living/LivingDamageEvent;setAmount(F)V", ordinal = 0, remap = false), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void compatWithApotheosis(LivingDamageEvent event, CallbackInfo ci, int enchantLvl, Random temp) {
        if (enchantLvl > 5) {
            event.setCanceled(true);
            ci.cancel();
        }
    }

    @ModifyConstant(method = "doCorruptedKindredEnchantmentEvent", constant = @Constant(floatValue = 0.5F))
    private static float baseCommonDmgReduction(float constant) {
        return FlowingAgonyExtraConfig.commonUndeadDamageReductionPercent.get().floatValue();
    }

    @ModifyConstant(method = "doCorruptedKindredEnchantmentEvent", constant = @Constant(floatValue = 0.1F, ordinal = 1))
    private static float baseRareDmgReduction(float constant) {
        return FlowingAgonyExtraConfig.rareUndeadDamageReductionPercent.get().floatValue();
    }

    @ModifyConstant(method = "doCorruptedKindredEnchantmentEvent", constant = @Constant(floatValue = 0.05F))
    private static float baseWitherDmgReduction(float constant) {
        return FlowingAgonyExtraConfig.witherDamageReductionPercent.get().floatValue();
    }

    @Inject(method = "doCorruptedKindredEnchantmentEvent", at = @At(value = "INVOKE", target = "Llove/marblegate/flowingagony/util/EntityUtil;isCommonUndead(Lnet/minecraft/world/entity/LivingEntity;)Z", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void compatWithApotheosis2(LivingDamageEvent event, CallbackInfo ci, int enchantLvl, Random temp) {
        LivingEntity entity = event.getEntityLiving();
        if (entity.hasEffect(EffectRegistry.CURSE_OF_UNDEAD.get())) return;
        if (enchantLvl > 6 && temp.nextInt(100) < 5) {
            entity.addEffect(new MobEffectInstance(EffectRegistry.CURSE_OF_UNDEAD.get(), Integer.MAX_VALUE));
        }
    }

    @ModifyConstant(method = "doCorruptedKindredEnchantmentEvent", constant = @Constant(intValue = 144000))
    private static int realInfinity(int constant) {
        return Integer.MAX_VALUE;
    }
}

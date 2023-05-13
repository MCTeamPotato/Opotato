package com.teampotato.opotato.mixin.opotato.flowingagony;

import love.marblegate.flowingagony.eventhandler.enchantment.LastWishEnchantmentEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static com.teampotato.opotato.config.PotatoCommonConfig.*;

@Mixin(value = LastWishEnchantmentEventHandler.class, remap = false)
public abstract class MixinLastWishEnchantmentEventHandler {
    @ModifyConstant(method = "doMorirsDeathwishEnchantmentEvent_mendOnHurt", constant = @Constant(intValue = 3))
    private static int morirsDeathWishGiveOnHurt(int i) {
        return MORIRS_DEATH_WISH_DURABILITY_AMOUNT.get();
    }

    @ModifyConstant(method = "doMorirsDeathwishEnchantmentEvent_mendOnHurt", constant = @Constant(doubleValue = 100.0))
    private static double morirsDeathWishMaxValidDamage(double constant) {
        return MORIRS_DEATH_WISH_MAX_DAMAGE_IN_COUNT.get();
    }

    @ModifyConstant(method = "doMorirsDeathwishEnchantmentEvent_mendOnDeath", constant = @Constant(intValue = 64))
    private static int morirsDeathWishGiveOnDeath(int constant) {
         return DURABILITY_MORIRS_DEATH_WISH_GIVE_ON_DEATH.get();
    }

    @ModifyConstant(method = "doMorirsDeathwishEnchantmentEvent_mendOnDeath", constant = @Constant(intValue = 12000))
    private static int morirsDeathWishCoolDown(int constant) {
        return MORIRS_DEATH_WISH_COOL_DOWN_TICKS.get();
    }

    @ModifyConstant(method = "doMorirsLifeboundEnchantmentEvent_mendOnHeal", constant = @Constant(doubleValue = 100.0))
    private static double morirsLifeBoundMaxValidDamage(double constant) {
        return MORIRS_LIFE_BOUND_MAX_DAMAGE_IN_COUNT.get();
    }

    @ModifyConstant(method = "doMorirsLifeboundEnchantmentEvent_mendOnHeal", constant = @Constant(intValue = 10))
    private static int morirsLifeBoundGiveOnHeal(int constant) {
        return MORIRS_LIFE_BOUND_DURABILITY_GIVE_ON_HEAL.get();
    }

    @ModifyConstant(method = "doMorirsLifeBoundEnchantmentEvent_damageOnDeath", constant = @Constant(intValue = 32))
    private static int morirsLifeBoundLostOnDeath(int constant) {
        return MORIRS_LIFE_BOUND_DURABILITY_LOST_ON_DEATH.get();
    }

    @ModifyConstant(method = "doGuidensRegretEnchantmentEvent", constant = @Constant(intValue = 3))
    private static int guidensRegretGiveOnKillingEntity(int constant) {
         return GUIDENS_REGRET_DURABILITY_GIVE_ON_KILL.get();
    }

    @ModifyConstant(method = "doLastSweetDreamEnchantmentEvent_saveItem", constant = @Constant(floatValue = 0.9F))
    private static float lastSweetDreamSaveItemOnToss(float constant) {
        return (1.0F - LAST_SWEET_DREAM_DISAPPEAR_PERCENT.get());
    }
}

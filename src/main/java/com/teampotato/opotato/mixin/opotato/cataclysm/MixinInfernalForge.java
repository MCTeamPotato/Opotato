package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.infernal_forge;
import com.teampotato.opotato.config.mods.CataclysmExtraConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(infernal_forge.class)
public abstract class MixinInfernalForge extends PickaxeItem {
    public MixinInfernalForge(Tier arg, int i, float f, Properties arg2) {
        super(arg, i, f, arg2);
    }

    @ModifyConstant(method = "useOn", constant = @Constant(intValue = 80))
    private int onAddCoolDown(int constant) {
        return CataclysmExtraConfig.infernalForgeCoolDown.get();
    }

    @Inject(method = "setDamage", at = @At("HEAD"), cancellable = true, remap = false)
    private void onSetDamage(ItemStack stack, int damage, CallbackInfo ci) {
        if (CataclysmExtraConfig.infernalForgeCanBeDamaged.get()) {
            super.setDamage(stack, damage);
            ci.cancel();
        }
    }

    @ModifyConstant(method = "EarthQuake", constant = @Constant(intValue = 5))
    private int onSetOnFire(int constant) {
        return CataclysmExtraConfig.infernalForgeFireDuration.get();
    }

    @ModifyConstant(method = "EarthQuake", constant = @Constant(doubleValue = 4.0, ordinal = 0), remap = false)
    private double onGetRadius(double constant) {
        return CataclysmExtraConfig.infernalForgeEarthQuakeRadius.get();
    }

    /**
     * @author Kasualix
     * @reason impl config
     */
    @Overwrite
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return CataclysmExtraConfig.canInfernalForgeDisableShield.get();
    }

    /**
     * @author Kasualix
     * @reason impl config
     */
    @Overwrite
    public int getEnchantmentValue() {
        return CataclysmExtraConfig.infernalForgeEnchantmentValue.get();
    }

    @Inject(method = "canApplyAtEnchantingTable", at = @At("HEAD"), cancellable = true)
    private void onCheckEnchant(ItemStack stack, Enchantment enchantment, CallbackInfoReturnable<Boolean> cir) {
        if (CataclysmExtraConfig.infernalForgeCanBeDamaged.get()) {
            cir.setReturnValue((enchantment.category == EnchantmentCategory.BREAKABLE || enchantment.category == EnchantmentCategory.WEAPON || enchantment.category == EnchantmentCategory.DIGGER) && !(enchantment == Enchantments.SWEEPING_EDGE));
        }
    }

    @ModifyConstant(method = "hurtEnemy", constant = @Constant(floatValue = 1.0F))
    private float onGetKnockBackStrength(float constant) {
        return CataclysmExtraConfig.infernalForgeAttackKnockBack.get().floatValue();
    }
}

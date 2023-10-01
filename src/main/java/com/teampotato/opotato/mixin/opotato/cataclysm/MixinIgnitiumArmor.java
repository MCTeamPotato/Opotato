package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.Ignitium_Armor;
import com.teampotato.opotato.config.mods.CataclysmExtraConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Ignitium_Armor.class, remap = false)
public abstract class MixinIgnitiumArmor extends ArmorItem {
    public MixinIgnitiumArmor(ArmorMaterial arg, EquipmentSlot arg2, Properties arg3) {
        super(arg, arg2, arg3);
    }

    @Inject(method = "setDamage", at = @At("HEAD"), cancellable = true)
    private void onSetDamage(ItemStack stack, int damage, CallbackInfo ci) {
        if (CataclysmExtraConfig.ignitiumArmorCanBeDamaged.get()) {
            super.setDamage(stack, damage);
            ci.cancel();
        }
    }

    @Inject(method = "canApplyAtEnchantingTable", at = @At("HEAD"), cancellable = true)
    private void onCheckEnchant(ItemStack stack, Enchantment enchantment, CallbackInfoReturnable<Boolean> cir) {
        if (CataclysmExtraConfig.ignitiumArmorCanBeDamaged.get()) {
            EnchantmentCategory enchantmentCategory = enchantment.category;
            boolean isBreakable = enchantmentCategory == EnchantmentCategory.BREAKABLE;
            boolean isArmor = enchantmentCategory == EnchantmentCategory.ARMOR;
            if (this.slot == EquipmentSlot.HEAD) {
                cir.setReturnValue(isBreakable || isArmor || enchantmentCategory == EnchantmentCategory.ARMOR_HEAD);
            } else if (this.slot == EquipmentSlot.CHEST) {
                cir.setReturnValue(isBreakable || isArmor || enchantmentCategory == EnchantmentCategory.ARMOR_CHEST);
            } else if (this.slot == EquipmentSlot.LEGS) {
                cir.setReturnValue(isBreakable || isArmor || enchantmentCategory == EnchantmentCategory.ARMOR_LEGS);
            } else if (this.slot == EquipmentSlot.FEET) {
                cir.setReturnValue(isBreakable || isArmor || enchantmentCategory == EnchantmentCategory.ARMOR_FEET);
            } else {
                cir.setReturnValue(super.canApplyAtEnchantingTable(stack, enchantment));
            }
        }
    }
}

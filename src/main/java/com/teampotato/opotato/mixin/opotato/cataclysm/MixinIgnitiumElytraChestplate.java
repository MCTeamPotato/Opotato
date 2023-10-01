package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.Ignitium_Elytra_Chestplate;
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

@Mixin(value = Ignitium_Elytra_Chestplate.class, remap = false)
public class MixinIgnitiumElytraChestplate extends ArmorItem {
    public MixinIgnitiumElytraChestplate(ArmorMaterial arg, EquipmentSlot arg2, Properties arg3) {
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
    private void onEnchantCheck(ItemStack stack, Enchantment enchantment, CallbackInfoReturnable<Boolean> cir) {
        if (CataclysmExtraConfig.ignitiumArmorCanBeDamaged.get()) {
            cir.setReturnValue(enchantment.category == EnchantmentCategory.BREAKABLE || enchantment.category == EnchantmentCategory.ARMOR || enchantment.category == EnchantmentCategory.ARMOR_CHEST);
        }
    }
}

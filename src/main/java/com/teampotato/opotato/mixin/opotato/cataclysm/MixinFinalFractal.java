package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.final_fractal;
import com.teampotato.opotato.config.CataclysmExtraConfig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(final_fractal.class)
public class MixinFinalFractal extends SwordItem {
    public MixinFinalFractal(Tier arg, int i, float f, Properties arg2) {
        super(arg, i, f, arg2);
    }

    @Inject(method = "setDamage", at = @At("HEAD"), cancellable = true, remap = false)
    private void onSetDamage(ItemStack stack, int damage, CallbackInfo ci) {
        if (CataclysmExtraConfig.finalFractalCanBeDamaged.get()) {
            super.setDamage(stack, damage);
            ci.cancel();
        }
    }

    /**
     * @author Kasualix
     * @reason impl config
     */
    @Overwrite
    public boolean isValidRepairItem(ItemStack itemStack, ItemStack itemStackMaterial) {
        return CataclysmExtraConfig.finalFractalValidRepairItem.get().contains(Objects.requireNonNull(itemStack.getItem().getRegistryName()).toString()) ||
                CataclysmExtraConfig.finalFractalValidRepairItem.get().contains(Objects.requireNonNull(itemStackMaterial.getItem().getRegistryName()).toString());
    }

    @Inject(method = "canApplyAtEnchantingTable", at = @At("HEAD"), cancellable = true, remap = false)
    private void onCheckEnchant(ItemStack stack, Enchantment enchantment, CallbackInfoReturnable<Boolean> cir) {
        if (CataclysmExtraConfig.finalFractalCanBeDamaged.get()) {
            cir.setReturnValue(enchantment.category == EnchantmentCategory.BREAKABLE || enchantment.category == EnchantmentCategory.WEAPON);
            cir.cancel();
        }
    }
}

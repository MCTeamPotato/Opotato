package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.zweiender;
import com.teampotato.opotato.config.mods.CataclysmExtraConfig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(zweiender.class)
public abstract class MixinZweiender extends SwordItem {
    public MixinZweiender(Tier arg, int i, float f, Properties arg2) {
        super(arg, i, f, arg2);
    }

    @Inject(method = "setDamage", at = @At("HEAD"), cancellable = true, remap = false)
    private void onSetDamage(ItemStack stack, int damage, CallbackInfo ci) {
        if (CataclysmExtraConfig.zweienderCanBeDamaged.get()) {
            super.setDamage(stack, damage);
            ci.cancel();
        }
    }

    @Inject(method = "canApplyAtEnchantingTable", remap = false, at = @At("HEAD"), cancellable = true)
    private void onCheckEnchant(ItemStack stack, Enchantment enchantment, CallbackInfoReturnable<Boolean> cir) {
        if (CataclysmExtraConfig.zweienderCanBeDamaged.get()) {
            cir.setReturnValue(enchantment.category == EnchantmentCategory.BREAKABLE || enchantment.category == EnchantmentCategory.WEAPON);
            cir.cancel();
        }
    }

    /**
     * @author Kasualix
     * @reason impl config
     */
    @Overwrite
    public boolean isValidRepairItem(@NotNull ItemStack itemStack, ItemStack itemStackMaterial) {
        return CataclysmExtraConfig.zweienderHelmetValidRepairItem.get().contains(Objects.requireNonNull(itemStack.getItem().getRegistryName()).toString()) || CataclysmExtraConfig.zweienderHelmetValidRepairItem.get().contains(Objects.requireNonNull(itemStackMaterial.getItem().getRegistryName()).toString());
    }
}

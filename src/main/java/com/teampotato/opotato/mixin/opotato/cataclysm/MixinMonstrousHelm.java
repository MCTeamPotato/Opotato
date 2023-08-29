package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.Monstrous_Helm;
import com.teampotato.opotato.config.mods.CataclysmExtraConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Monstrous_Helm.class)
public abstract class MixinMonstrousHelm extends ArmorItem {
    public MixinMonstrousHelm(ArmorMaterial arg, EquipmentSlot arg2, Properties arg3) {
        super(arg, arg2, arg3);
    }

    @Inject(method = "setDamage", at = @At("HEAD"), cancellable = true, remap = false)
    private void onSetDamage(ItemStack stack, int damage, CallbackInfo ci) {
        if (CataclysmExtraConfig.monstrousHelmetCanBeDamaged.get()) {
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
        return CataclysmExtraConfig.monstrousHelmetValidRepairItem.get().contains(Objects.requireNonNull(itemStack.getItem().getRegistryName()).toString()) || CataclysmExtraConfig.monstrousHelmetValidRepairItem.get().contains(Objects.requireNonNull(itemStackMaterial.getItem().getRegistryName()).toString());
    }

    @ModifyConstant(method = "onArmorTick", constant = @Constant(intValue = 350), remap = false)
    private int onGetCoolDown(int constant) {
        return CataclysmExtraConfig.monstrousHelmetCoolDown.get();
    }

    @ModifyConstant(method = "onArmorTick", constant = @Constant(floatValue = 1.5F), remap = false)
    private float onGetExplosionRadius(float constant) {
        return CataclysmExtraConfig.monstrousHelmetExplosionRadius.get().floatValue();
    }

    @ModifyConstant(method = "onArmorTick", constant = @Constant(doubleValue = 4.0), remap = false)
    private double onGetKnockBackRadius(double constant) {
        return CataclysmExtraConfig.monstrousHelmetKnockBackRadius.get();
    }

    @Inject(method = "canApplyAtEnchantingTable", at = @At("HEAD"), cancellable = true, remap = false)
    private void onCheckEnchant(ItemStack stack, Enchantment enchantment, CallbackInfoReturnable<Boolean> cir) {
        if (CataclysmExtraConfig.monstrousHelmetCanBeDamaged.get()) {
            cir.setReturnValue(enchantment.category == EnchantmentCategory.BREAKABLE || enchantment.category == EnchantmentCategory.ARMOR || enchantment.category == EnchantmentCategory.ARMOR_HEAD);
            cir.cancel();
        }
    }

    @ModifyArg(method = "onArmorTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;explode(Lnet/minecraft/world/entity/Entity;DDDFLnet/minecraft/world/level/Explosion$BlockInteraction;)Lnet/minecraft/world/level/Explosion;"))
    private Explosion.BlockInteraction onInteractBlock(Explosion.BlockInteraction blockInteraction) {
        return Explosion.BlockInteraction.valueOf(CataclysmExtraConfig.monstrousHelmetExplosionBlockInteraction.get());
    }

    @ModifyConstant(method = "onArmorTick", constant = @Constant(intValue = 200), remap = false)
    private int onGetMonstrousDuration(int constant) {
        return CataclysmExtraConfig.monstrousHelmetMonstrousEffectDuration.get();
    }
}

package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.Ignitium_Elytra_Chestplate;
import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = Ignitium_Elytra_Chestplate.class, remap = false)
public abstract class MixinIgnitiumFlytraChestplate extends ArmorItem {
    public MixinIgnitiumFlytraChestplate(IArmorMaterial pMaterial, EquipmentSlotType pSlot, Properties pProperties) {
        super(pMaterial, pSlot, pProperties);
    }

    /**
     * @author Kasualix
     * @reason fix damage
     */
    @Overwrite
    public void setDamage(ItemStack stack, int damage) {
        if (PotatoCommonConfig.IGNITIUM_ARMOR_HAS_DAMAGE.get()) {
            super.setDamage(stack, damage);
        } else {
            super.setDamage(stack, 0);
        }
    }
}

package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.Monstrous_Helm;
import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = Monstrous_Helm.class, remap = false)
public abstract class MixinMonstrousHelm extends ArmorItem {
    public MixinMonstrousHelm(IArmorMaterial pMaterial, EquipmentSlotType pSlot, Properties pProperties) {
        super(pMaterial, pSlot, pProperties);
    }

    /**
     * @author Kasualix
     * @reason fix damage
     */
    @Overwrite
    public void setDamage(ItemStack stack, int damage) {
        if (PotatoCommonConfig.MONSTROUS_HELM_HAS_DAMAGE.get()) {
            super.setDamage(stack, damage);
        } else {
            super.setDamage(stack, 0);
        }
    }
}

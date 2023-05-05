package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.zweiender;
import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = zweiender.class, remap = false)
public abstract class MixinZweiender extends SwordItem {
    public MixinZweiender(IItemTier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    /**
     * @author Kasualix
     * @reason fix damage
     */
    @Overwrite
    public void setDamage(ItemStack stack, int damage) {
        if (PotatoCommonConfig.ZWEIENDER_HAS_DAMAGE.get()) {
            super.setDamage(stack, damage);
        } else {
            super.setDamage(stack, 0);
        }
    }
}

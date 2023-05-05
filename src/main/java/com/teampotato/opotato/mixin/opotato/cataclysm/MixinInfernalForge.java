package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.infernal_forge;
import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.CooldownTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = infernal_forge.class)
public abstract class MixinInfernalForge extends PickaxeItem {
    public MixinInfernalForge(IItemTier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Redirect(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/CooldownTracker;addCooldown(Lnet/minecraft/item/Item;I)V"))
    private void redirectUseOn(CooldownTracker instance, Item item, int ticks) {
        instance.addCooldown((infernal_forge)(Object)this, PotatoCommonConfig.INFERNAL_FORGE_COOL_DOWN.get());
    }

    /**
     * @author Kasualix
     * @reason fix damage
     */
    @Overwrite(remap = false)
    public void setDamage(ItemStack stack, int damage) {
        if (PotatoCommonConfig.INFERNAL_FORGE_HAS_DAMAGE.get()) {
            super.setDamage(stack, damage);
        } else {
            super.setDamage(stack, 0);
        }
    }
}

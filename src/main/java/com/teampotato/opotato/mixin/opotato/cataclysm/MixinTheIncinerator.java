package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.The_Incinerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.teampotato.opotato.config.PotatoCommonConfig.*;

@Mixin(value = The_Incinerator.class)
public abstract class MixinTheIncinerator {
    @Redirect(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/CooldownTracker;addCooldown(Lnet/minecraft/item/Item;I)V"))
    private void onAddCoolDown(CooldownTracker instance, Item item, int time) {
        instance.addCooldown((The_Incinerator)(Object)this, INCINERATOR_COOL_DOWN.get());
    }

    /**
     * @author Kasualix
     * @reason implement config
     */
    @Overwrite
    public int getUseDuration(@NotNull ItemStack pStack) {
        return INCINERATOR_USE_DURATION.get();
    }

    /**
     * @author Kasualix
     * @reason implement config
     */
    @Overwrite
    public int getEnchantmentValue() {
        return INCINERATOR_ENCHANTMENT_VALUE.get();
    }
}

package com.teampotato.opotato.mixin.opotato.quark;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.quark.content.tools.module.AncientTomesModule;

@Mixin(AncientTomesModule.class)
public abstract class AncientTomesModuleMixin {
    @Redirect(method = "onAnvilUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;", ordinal = 1))
    private Item onCheckEnchantedBook(ItemStack instance) {
        return Items.AIR;
    }
}

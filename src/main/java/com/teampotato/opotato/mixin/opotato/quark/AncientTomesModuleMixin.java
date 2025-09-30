package com.teampotato.opotato.mixin.opotato.quark;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.AnvilUpdateEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.content.tools.module.AncientTomesModule;

import java.util.Map;

@Mixin(AncientTomesModule.class)
public abstract class AncientTomesModuleMixin {
    @Inject(method = "onAnvilUpdate", at = @At("HEAD"), cancellable = true, remap = false)
    private void fixOverLevelBook(@NotNull AnvilUpdateEvent event, CallbackInfo ci) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        if (!left.isEmpty() && !right.isEmpty() && right.getItem() == Items.ENCHANTED_BOOK) {
            Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(right);
            Map<Enchantment, Integer> currentEnchants = EnchantmentHelper.getEnchantments(left);

            boolean modified = false;

            for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                Enchantment enchantment = entry.getKey();
                if (enchantment == null) continue;

                int level = entry.getValue();
                if (enchantment.canEnchant(left)) {
                    int appliedLevel = Math.min(level, enchantment.getMaxLevel());

                    boolean compatible = true;
                    for (Enchantment comparing : currentEnchants.keySet()) {
                        if (comparing != enchantment && !comparing.isCompatibleWith(enchantment)) {
                            compatible = false;
                            break;
                        }
                    }

                    if (compatible) {
                        currentEnchants.put(enchantment, appliedLevel);
                        modified = true;
                    }
                }
            }

            if (modified) {
                ItemStack out = left.copy();
                EnchantmentHelper.setEnchantments(currentEnchants, out);

                String name = event.getName();
                int cost = left.getBaseRepairCost() + 1;
                if (name != null && !name.isEmpty() && (!out.hasCustomHoverName() || !out.getHoverName().getString().equals(name))) {
                    out.setHoverName(new TextComponent(name));
                    ++cost;
                }

                event.setOutput(out);
                event.setCost(cost);

                ci.cancel();
            }
        }
    }
}

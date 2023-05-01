package com.teampotato.opotato.mixin.opotato.arsnouveau;

import com.hollingsworth.arsnouveau.api.loot.LootTables;
import com.hollingsworth.arsnouveau.common.potions.ModPotions;
import com.hollingsworth.arsnouveau.setup.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

@Mixin(value = LootTables.class, remap = false)
public class MixinLootTables {
    @Shadow
    public static Random r;
    private static List<Supplier<ItemStack>> NEW_BASIC_LOOT = new ArrayList<>();

    static {
        if (!PotatoCommonConfig.DISABLE_ARS_NOUVEAU_MANA_GEM_IN_BASIC_LOOT.get()) NEW_BASIC_LOOT.add(() -> new ItemStack(ItemsRegistry.manaGem,1 + r.nextInt(5)));
        NEW_BASIC_LOOT.add(() -> new ItemStack(ItemsRegistry.WILDEN_HORN,1 + r.nextInt(3)));
        NEW_BASIC_LOOT.add(() -> new ItemStack(ItemsRegistry.WILDEN_SPIKE, 1 + r.nextInt(3)));
        NEW_BASIC_LOOT.add(() -> new ItemStack(ItemsRegistry.WILDEN_WING, 1 + r.nextInt(3)));
        NEW_BASIC_LOOT.add(() -> new ItemStack(BlockRegistry.MANA_BERRY_BUSH, 1 + r.nextInt(3)));
        NEW_BASIC_LOOT.add(() ->{
            ItemStack stack = new ItemStack(Items.POTION);
            PotionUtils.setPotion(stack, ModPotions.LONG_MANA_REGEN_POTION);
            return stack;
        });
        NEW_BASIC_LOOT.add(() ->{
            ItemStack stack = new ItemStack(Items.POTION);
            PotionUtils.setPotion(stack, ModPotions.STRONG_MANA_REGEN_POTION);
            return stack;
        });

        NEW_BASIC_LOOT.add(() ->{
            ItemStack stack = new ItemStack(Items.POTION);
            PotionUtils.setPotion(stack, ModPotions.MANA_REGEN_POTION);
            return stack;
        });
    }

    @Redirect(method = "getRandomRoll", at = @At(value = "FIELD", target = "Lcom/hollingsworth/arsnouveau/api/loot/LootTables;BASIC_LOOT:Ljava/util/List;"))
    private static List<Supplier<ItemStack>> onGetRandomBasicLot() {
        return NEW_BASIC_LOOT;
    }
}

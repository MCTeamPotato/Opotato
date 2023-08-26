package com.teampotato.opotato.mixin.opotato.arsnouveau;

import com.google.common.collect.Lists;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.loot.LootTables;
import com.hollingsworth.arsnouveau.common.items.RitualTablet;
import com.hollingsworth.arsnouveau.common.potions.ModPotions;
import com.hollingsworth.arsnouveau.setup.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import static com.teampotato.opotato.config.ArsNouveauLootConfig.*;

@Mixin(LootTables.class)
public class MixinLootTables {
    @Unique
    private static final Random r = ThreadLocalRandom.current();
    @Unique
    private static final List<Supplier<ItemStack>> NEW_BASIC_LOOT = Lists.newArrayList();

    @Unique
    private static final List<Supplier<ItemStack>> NEW_UNCOMMON_LOOT = Lists.newArrayList();

    @Redirect(method = "getRandomRoll", at = @At(value = "FIELD", target = "Lcom/hollingsworth/arsnouveau/api/loot/LootTables;BASIC_LOOT:Ljava/util/List;"))
    private static List<Supplier<ItemStack>> onGetBasicLoot() {
        if (NEW_BASIC_LOOT.isEmpty()) {
            if(manaGem.get()) NEW_BASIC_LOOT.add(() -> new ItemStack(ItemsRegistry.manaGem, 1 + r.nextInt(manaGemMaxCount.get())));
            if(wildenHorn.get()) NEW_BASIC_LOOT.add(() -> new ItemStack(ItemsRegistry.WILDEN_HORN, 1 + r.nextInt(wildenHornMaxCount.get())));
            if(wildenSpike.get()) NEW_BASIC_LOOT.add(() -> new ItemStack(ItemsRegistry.WILDEN_SPIKE, 1 + r.nextInt(wildenSpikeMaxCount.get())));
            if(wildenWing.get()) NEW_BASIC_LOOT.add(() -> new ItemStack(ItemsRegistry.WILDEN_WING, 1 + r.nextInt(wildenWingMaxCount.get())));
            if(manaBerryBush.get()) NEW_BASIC_LOOT.add(() -> new ItemStack(BlockRegistry.MANA_BERRY_BUSH, 1 + r.nextInt(manaBerryBushMaxCount.get())));
            if(longManaRegenPotion.get()) NEW_BASIC_LOOT.add(() -> {
                ItemStack stack = new ItemStack(Items.POTION);
                PotionUtils.setPotion(stack, ModPotions.LONG_MANA_REGEN_POTION);
                return stack;
            });
            if(strongManaRegenPotion.get()) NEW_BASIC_LOOT.add(() -> {
                ItemStack stack = new ItemStack(Items.POTION);
                PotionUtils.setPotion(stack, ModPotions.STRONG_MANA_REGEN_POTION);
                return stack;
            });
            if(manaRegenPotion.get()) NEW_BASIC_LOOT.add(() -> {
                ItemStack stack = new ItemStack(Items.POTION);
                PotionUtils.setPotion(stack, ModPotions.MANA_REGEN_POTION);
                return stack;
            });
        }
        return NEW_BASIC_LOOT;
    }

    @Redirect(method = "getRandomRoll", at = @At(value = "FIELD", target = "Lcom/hollingsworth/arsnouveau/api/loot/LootTables;UNCOMMON_LOOT:Ljava/util/List;"))
    private static List<Supplier<ItemStack>> onGetUncommonLoot() {
        if (NEW_UNCOMMON_LOOT.isEmpty()) {
            if(warpScroll.get()) NEW_UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.warpScroll, 1 + r.nextInt(2)));
            if(carbuncleShard.get()) NEW_UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.carbuncleShard));
            if(sylphShard.get()) NEW_UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.sylphShard));
            if(drygmyShard.get()) NEW_UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.DRYGMY_SHARD));
            if(wixieShard.get()) NEW_UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.WIXIE_SHARD));
            if(amplifyArrow.get()) NEW_UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.AMPLIFY_ARROW, 16 + r.nextInt(16)));
            if(splitArrow.get()) NEW_UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.SPLIT_ARROW, 16 + r.nextInt(16)));
            if(pierceArrow.get()) NEW_UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.PIERCE_ARROW, 16 + r.nextInt(16)));
            if(ritualTablets.get()) NEW_UNCOMMON_LOOT.add(() -> {
                List<RitualTablet> tablets = Lists.newArrayList(ArsNouveauAPI.getInstance().getRitualItemMap().values());
                return new ItemStack(tablets.get(r.nextInt(tablets.size())));
            });
        }
        return NEW_UNCOMMON_LOOT;
    }//TODO
}

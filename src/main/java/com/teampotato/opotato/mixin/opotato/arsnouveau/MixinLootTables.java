package com.teampotato.opotato.mixin.opotato.arsnouveau;

import com.google.common.collect.Lists;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.loot.LootTables;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.common.items.RitualTablet;
import com.hollingsworth.arsnouveau.common.potions.ModPotions;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.common.spell.effect.*;
import com.hollingsworth.arsnouveau.common.spell.method.*;
import com.hollingsworth.arsnouveau.setup.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import static com.teampotato.opotato.config.ArsNouveauLootConfig.*;

@Mixin(LootTables.class)
public abstract class MixinLootTables {
    @Shadow public static ItemStack makeTome(String name, Spell spell) {
        throw new RuntimeException();
    }

    @Shadow public static ItemStack makeTome(String name, Spell spell, String flavorText){
        throw new RuntimeException();
    }

    @Unique
    private static final Random potato$randomGen = ThreadLocalRandom.current();
    @Unique
    private static final List<Supplier<ItemStack>> NEW_BASIC_LOOT = Lists.newArrayList();

    @Unique
    private static final List<Supplier<ItemStack>> NEW_UNCOMMON_LOOT = Lists.newArrayList();

    @Unique
    private static final List<Supplier<ItemStack>> NEW_RARE_LOOT = Lists.newArrayList();

    @Redirect(method = "getRandomRoll", at = @At(value = "FIELD", target = "Lcom/hollingsworth/arsnouveau/api/loot/LootTables;BASIC_LOOT:Ljava/util/List;"))
    private static List<Supplier<ItemStack>> onGetBasicLoot() {
        if (NEW_BASIC_LOOT.isEmpty()) {
            if(manaGem.get()) NEW_BASIC_LOOT.add(() -> new ItemStack(ItemsRegistry.manaGem, 1 + potato$randomGen.nextInt(manaGemMaxCount.get())));
            if(wildenHorn.get()) NEW_BASIC_LOOT.add(() -> new ItemStack(ItemsRegistry.WILDEN_HORN, 1 + potato$randomGen.nextInt(wildenHornMaxCount.get())));
            if(wildenSpike.get()) NEW_BASIC_LOOT.add(() -> new ItemStack(ItemsRegistry.WILDEN_SPIKE, 1 + potato$randomGen.nextInt(wildenSpikeMaxCount.get())));
            if(wildenWing.get()) NEW_BASIC_LOOT.add(() -> new ItemStack(ItemsRegistry.WILDEN_WING, 1 + potato$randomGen.nextInt(wildenWingMaxCount.get())));
            if(manaBerryBush.get()) NEW_BASIC_LOOT.add(() -> new ItemStack(BlockRegistry.MANA_BERRY_BUSH, 1 + potato$randomGen.nextInt(manaBerryBushMaxCount.get())));
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
            if(warpScroll.get()) NEW_UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.warpScroll, warpScrollBasicCount.get() + potato$randomGen.nextInt(warpScrollMaxCount.get())));
            if(carbuncleShard.get()) NEW_UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.carbuncleShard));
            if(sylphShard.get()) NEW_UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.sylphShard));
            if(drygmyShard.get()) NEW_UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.DRYGMY_SHARD));
            if(wixieShard.get()) NEW_UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.WIXIE_SHARD));
            if(amplifyArrow.get()) NEW_UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.AMPLIFY_ARROW, amplifyArrowBasicCount.get() + potato$randomGen.nextInt(amplifyArrowMaxCount.get())));
            if(splitArrow.get()) NEW_UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.SPLIT_ARROW, splitArrowBasicCount.get() + potato$randomGen.nextInt(splitArrowMaxCount.get())));
            if(pierceArrow.get()) NEW_UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.PIERCE_ARROW, pierceArrowBasicCount.get() + potato$randomGen.nextInt(pierceArrowMaxCount.get())));
            if(ritualTablets.get()) NEW_UNCOMMON_LOOT.add(() -> {
                Collection<RitualTablet> ritualTablets = ArsNouveauAPI.getInstance().getRitualItemMap().values();
                return new ItemStack(ritualTablets.stream().skip(potato$randomGen.nextInt(ritualTablets.size())).findFirst().orElse(null));
            });
        }
        return NEW_UNCOMMON_LOOT;
    }

    @Redirect(method = "getRandomRoll", at = @At(value = "FIELD", target = "Lcom/hollingsworth/arsnouveau/api/loot/LootTables;RARE_LOOT:Ljava/util/List;"))
    private static List<Supplier<ItemStack>> onGetRareLoot() {
        if (NEW_RARE_LOOT.isEmpty()) {
            NEW_RARE_LOOT.add(() -> makeTome(
                    I18n.get("opotato.arsnouveau.tome.xacris_tiny_hut"),
                            (new Spell()).add(MethodUnderfoot.INSTANCE).add(EffectPhantomBlock.INSTANCE).add(AugmentAOE.INSTANCE, 3).add(AugmentPierce.INSTANCE, 3),
                            I18n.get("opotato.arsnouveau.tome.xacris_tiny_hut.flavorText")
            ));
            NEW_RARE_LOOT.add(() -> makeTome(
                    I18n.get("opotato.arsnouveau.tome.glow_trap"),
                    (new Spell()).add(MethodTouch.INSTANCE).add(EffectRune.INSTANCE).add(EffectSnare.INSTANCE).add(AugmentExtendTime.INSTANCE).add(EffectLight.INSTANCE),
                    I18n.get("opotato.arsnouveau.tome.glow_trap.flavorText")
            ));
            NEW_RARE_LOOT.add(() -> makeTome(
                    I18n.get("opotato.arsnouveau.tome.baileys_bovine_rocket"),
                    (new Spell()).add(MethodProjectile.INSTANCE).add(EffectLaunch.INSTANCE).add(AugmentAmplify.INSTANCE, 2).add(EffectDelay.INSTANCE).add(EffectExplosion.INSTANCE).add(AugmentAmplify.INSTANCE)
            ));
            NEW_RARE_LOOT.add(() -> makeTome(
                    I18n.get("opotato.arsnouveau.tome.arachnes_weaving"),
                    (new Spell()).add(MethodProjectile.INSTANCE).add(AugmentSplit.INSTANCE, 2).add(EffectSnare.INSTANCE).add(AugmentExtendTime.INSTANCE).add(AugmentExtendTime.INSTANCE),
                    I18n.get("opotato.arsnouveau.tome.arachnes_weaving.flavorText")
            ));
            NEW_RARE_LOOT.add(() -> makeTome(
                    I18n.get("opotato.arsnouveau.tome.warp_impact"),
                    (new Spell()).add(MethodProjectile.INSTANCE).add(EffectBlink.INSTANCE).add(EffectExplosion.INSTANCE).add(AugmentAOE.INSTANCE),
                    I18n.get("opotato.arsnouveau.tome.warp_impact.flavorText")
            ));
            NEW_RARE_LOOT.add(() -> makeTome(
                    I18n.get("opotato.arsnouveau.tome.ffff"),
                    (new Spell()).add(MethodProjectile.INSTANCE).add(EffectIgnite.INSTANCE).add(EffectDelay.INSTANCE).add(EffectConjureWater.INSTANCE).add(EffectFreeze.INSTANCE),
                    I18n.get("opotato.arsnouveau.tome.ffff.flavorText")
            ));
            NEW_RARE_LOOT.add(() -> makeTome(
                    I18n.get("opotato.arsnouveau.tome.gootastics_telekinetic_fishing_rod"),
                    (new Spell()).add(MethodProjectile.INSTANCE).add(EffectLaunch.INSTANCE).add(AugmentAmplify.INSTANCE, 2).add(EffectDelay.INSTANCE).add(EffectPull.INSTANCE).add(AugmentAmplify.INSTANCE, 2),
                    I18n.get("opotato.arsnouveau.tome.gootastics_telekinetic_fishing_rod.flavorText")
            ));
            NEW_RARE_LOOT.add(() -> makeTome(
                    I18n.get("opotato.arsnouveau.tome.potent_toxin"),
                    (new Spell()).add(MethodProjectile.INSTANCE).add(EffectHex.INSTANCE).add(EffectHarm.INSTANCE).add(AugmentExtendTime.INSTANCE),
                    I18n.get("opotato.arsnouveau.tome.potent_toxin.flavorText")
            ));
            NEW_RARE_LOOT.add(() -> makeTome(
                    I18n.get("opotato.arsnouveau.tome.the_shadows_temp_tunnel"),
                    (new Spell()).add(MethodTouch.INSTANCE).add(EffectIntangible.INSTANCE).add(AugmentAOE.INSTANCE, 2).add(AugmentPierce.INSTANCE, 5).add(AugmentExtendTime.INSTANCE),
                    I18n.get("opotato.arsnouveau.tome.the_shadows_temp_tunnel.flavorText")
            ));
            NEW_RARE_LOOT.add(() -> makeTome(
                    I18n.get("opotato.arsnouveau.tome.vault"),
                    (new Spell()).add(MethodSelf.INSTANCE).add(EffectLaunch.INSTANCE).add(EffectDelay.INSTANCE).add(EffectLeap.INSTANCE).add(EffectSlowfall.INSTANCE),
                    I18n.get("opotato.arsnouveau.tome.vault.flavorText")
            ));
            NEW_RARE_LOOT.add(() -> makeTome(
                    I18n.get("opotato.arsnouveau.tome.fireball"),
                    (new Spell()).add(MethodProjectile.INSTANCE).add(EffectIgnite.INSTANCE).add(EffectExplosion.INSTANCE).add(AugmentAmplify.INSTANCE, 2).add(AugmentAOE.INSTANCE, 2),
                    I18n.get("opotato.arsnouveau.tome.fireball.flavorText")
            ));
            NEW_RARE_LOOT.add(() -> makeTome(
                    I18n.get("opotato.arsnouveau.tome.rune_of_renewing"),
                    (new Spell()).add(MethodTouch.INSTANCE).add(EffectRune.INSTANCE).add(EffectDispel.INSTANCE).add(EffectHeal.INSTANCE).add(AugmentExtendTime.INSTANCE),
                    I18n.get("opotato.arsnouveau.tome.rune_of_renewing.flavorText")
            ));
            NEW_RARE_LOOT.add(() -> makeTome(
                    I18n.get("opotato.arsnouveau.tome.knocked_out_of_orbit"),
                    (new Spell()).add(MethodOrbit.INSTANCE).add(EffectLaunch.INSTANCE).add(AugmentAmplify.INSTANCE, 2).add(EffectDelay.INSTANCE).add(EffectKnockback.INSTANCE).add(AugmentAmplify.INSTANCE, 2),
                    I18n.get("opotato.arsnouveau.tome.knocked_out_of_orbit.flavorText")
            ));
            NEW_RARE_LOOT.add(() -> makeTome(
                    I18n.get("opotato.arsnouveau.tome.takeoff"),
                    (new Spell()).add(MethodSelf.INSTANCE).add(EffectLaunch.INSTANCE, 2).add(EffectGlide.INSTANCE).add(AugmentDurationDown.INSTANCE),
                    I18n.get("opotato.arsnouveau.tome.takeoff.flavorText")
            ));
        }
        return NEW_RARE_LOOT;
    }
}

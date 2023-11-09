package com.teampotato.opotato.mixin.opotato.arsnouveau;

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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import static com.teampotato.opotato.config.mods.ArsNouveauLootConfig.*;

@Mixin(value = LootTables.class, remap = false)
public abstract class MixinLootTables {
    @Shadow public static List<Supplier<ItemStack>> BASIC_LOOT;
    @Shadow public static List<Supplier<ItemStack>> UNCOMMON_LOOT;
    @Shadow public static List<Supplier<ItemStack>> RARE_LOOT;
    @Unique private static final Random RANDOM_GENERATOR = ThreadLocalRandom.current();

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onInitLoots(CallbackInfo ci) {
        BASIC_LOOT.clear();
        if (manaGem.get()) BASIC_LOOT.add(() -> new ItemStack(ItemsRegistry.manaGem, manaGemBasicCount.get() + RANDOM_GENERATOR.nextInt(manaGemMaxCount.get())));
        if (wildenHorn.get()) BASIC_LOOT.add(() -> new ItemStack(ItemsRegistry.WILDEN_HORN, wildenHornBasicCount.get() + RANDOM_GENERATOR.nextInt(wildenHornMaxCount.get())));
        if (wildenSpike.get()) BASIC_LOOT.add(() -> new ItemStack(ItemsRegistry.WILDEN_SPIKE, wildenSpikeBasicCount.get() + RANDOM_GENERATOR.nextInt(wildenSpikeMaxCount.get())));
        if (wildenWing.get()) BASIC_LOOT.add(() -> new ItemStack(ItemsRegistry.WILDEN_WING, wildenWingBasicCount.get() + RANDOM_GENERATOR.nextInt(wildenWingMaxCount.get())));
        if (manaBerryBush.get()) BASIC_LOOT.add(() -> new ItemStack(BlockRegistry.MANA_BERRY_BUSH, manaBerryBushBasicCount.get() + RANDOM_GENERATOR.nextInt(manaBerryBushMaxCount.get())));
        if (longManaRegenPotion.get()) BASIC_LOOT.add(() -> {
            ItemStack stack = new ItemStack(Items.POTION);
            PotionUtils.setPotion(stack, ModPotions.LONG_MANA_REGEN_POTION);
            return stack;
        });
        if (strongManaRegenPotion.get()) BASIC_LOOT.add(() -> {
            ItemStack stack = new ItemStack(Items.POTION);
            PotionUtils.setPotion(stack, ModPotions.STRONG_MANA_REGEN_POTION);
            return stack;
        });
        if (manaRegenPotion.get()) BASIC_LOOT.add(() -> {
            ItemStack stack = new ItemStack(Items.POTION);
            PotionUtils.setPotion(stack, ModPotions.MANA_REGEN_POTION);
            return stack;
        });

        UNCOMMON_LOOT.clear();
        if (warpScroll.get()) UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.warpScroll, warpScrollBasicCount.get() + RANDOM_GENERATOR.nextInt(warpScrollMaxCount.get())));
        if (carbuncleShard.get()) UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.carbuncleShard));
        if (sylphShard.get()) UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.sylphShard));
        if (drygmyShard.get()) UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.DRYGMY_SHARD));
        if (wixieShard.get()) UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.WIXIE_SHARD));
        if (amplifyArrow.get()) UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.AMPLIFY_ARROW, amplifyArrowBasicCount.get() + RANDOM_GENERATOR.nextInt(amplifyArrowMaxCount.get())));
        if (splitArrow.get()) UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.SPLIT_ARROW, splitArrowBasicCount.get() + RANDOM_GENERATOR.nextInt(splitArrowMaxCount.get())));
        if (pierceArrow.get()) UNCOMMON_LOOT.add(() -> new ItemStack(ItemsRegistry.PIERCE_ARROW, pierceArrowBasicCount.get() + RANDOM_GENERATOR.nextInt(pierceArrowMaxCount.get())));
        if (ritualTablets.get()) UNCOMMON_LOOT.add(() -> {
            Collection<RitualTablet> ritualTablets = ArsNouveauAPI.getInstance().getRitualItemMap().values();
            return new ItemStack(ritualTablets.stream().skip(RANDOM_GENERATOR.nextInt(ritualTablets.size())).findFirst().orElse(null));
        });

        RARE_LOOT.clear();
        if (xacrisTinyHut.get()) RARE_LOOT.add(() -> LootTables.makeTome(I18n.get("opotato.arsnouveau.tome.xacris_tiny_hut"), (new Spell()).add(MethodUnderfoot.INSTANCE).add(EffectPhantomBlock.INSTANCE).add(AugmentAOE.INSTANCE, 3).add(AugmentPierce.INSTANCE, 3), I18n.get("opotato.arsnouveau.tome.xacris_tiny_hut.flavorText")));
        if (glowTrap.get()) RARE_LOOT.add(() -> LootTables.makeTome(I18n.get("opotato.arsnouveau.tome.glow_trap"), (new Spell()).add(MethodTouch.INSTANCE).add(EffectRune.INSTANCE).add(EffectSnare.INSTANCE).add(AugmentExtendTime.INSTANCE).add(EffectLight.INSTANCE), I18n.get("opotato.arsnouveau.tome.glow_trap.flavorText")));
        if (baileysBovineRocket.get()) RARE_LOOT.add(() -> LootTables.makeTome(I18n.get("opotato.arsnouveau.tome.baileys_bovine_rocket"), (new Spell()).add(MethodProjectile.INSTANCE).add(EffectLaunch.INSTANCE).add(AugmentAmplify.INSTANCE, 2).add(EffectDelay.INSTANCE).add(EffectExplosion.INSTANCE).add(AugmentAmplify.INSTANCE)));
        if (arachnesWeaving.get()) RARE_LOOT.add(() -> LootTables.makeTome(I18n.get("opotato.arsnouveau.tome.arachnes_weaving"), (new Spell()).add(MethodProjectile.INSTANCE).add(AugmentSplit.INSTANCE, 2).add(EffectSnare.INSTANCE).add(AugmentExtendTime.INSTANCE).add(AugmentExtendTime.INSTANCE), I18n.get("opotato.arsnouveau.tome.arachnes_weaving.flavorText")));
        if (warpImpact.get()) RARE_LOOT.add(() -> LootTables.makeTome(I18n.get("opotato.arsnouveau.tome.warp_impact"), (new Spell()).add(MethodProjectile.INSTANCE).add(EffectBlink.INSTANCE).add(EffectExplosion.INSTANCE).add(AugmentAOE.INSTANCE), I18n.get("opotato.arsnouveau.tome.warp_impact.flavorText")));
        if (farfallasFrostyFlames.get()) RARE_LOOT.add(() -> LootTables.makeTome(I18n.get("opotato.arsnouveau.tome.ffff"), (new Spell()).add(MethodProjectile.INSTANCE).add(EffectIgnite.INSTANCE).add(EffectDelay.INSTANCE).add(EffectConjureWater.INSTANCE).add(EffectFreeze.INSTANCE), I18n.get("opotato.arsnouveau.tome.ffff.flavorText")));
        if (gootasticsTelekineticFishingRod.get()) RARE_LOOT.add(() -> LootTables.makeTome(I18n.get("opotato.arsnouveau.tome.gootastics_telekinetic_fishing_rod"), (new Spell()).add(MethodProjectile.INSTANCE).add(EffectLaunch.INSTANCE).add(AugmentAmplify.INSTANCE, 2).add(EffectDelay.INSTANCE).add(EffectPull.INSTANCE).add(AugmentAmplify.INSTANCE, 2), I18n.get("opotato.arsnouveau.tome.gootastics_telekinetic_fishing_rod.flavorText")));
        if (potentToxin.get()) RARE_LOOT.add(() -> LootTables.makeTome(I18n.get("opotato.arsnouveau.tome.potent_toxin"), (new Spell()).add(MethodProjectile.INSTANCE).add(EffectHex.INSTANCE).add(EffectHarm.INSTANCE).add(AugmentExtendTime.INSTANCE), I18n.get("opotato.arsnouveau.tome.potent_toxin.flavorText")));
        if (theShadowsTemporaryTunnel.get()) RARE_LOOT.add(() -> LootTables.makeTome(I18n.get("opotato.arsnouveau.tome.the_shadows_temp_tunnel"), (new Spell()).add(MethodTouch.INSTANCE).add(EffectIntangible.INSTANCE).add(AugmentAOE.INSTANCE, 2).add(AugmentPierce.INSTANCE, 5).add(AugmentExtendTime.INSTANCE), I18n.get("opotato.arsnouveau.tome.the_shadows_temp_tunnel.flavorText")));
        if (vault.get()) RARE_LOOT.add(() -> LootTables.makeTome(I18n.get("opotato.arsnouveau.tome.vault"), (new Spell()).add(MethodSelf.INSTANCE).add(EffectLaunch.INSTANCE).add(EffectDelay.INSTANCE).add(EffectLeap.INSTANCE).add(EffectSlowfall.INSTANCE), I18n.get("opotato.arsnouveau.tome.vault.flavorText")));
        if (fireball.get()) RARE_LOOT.add(() -> LootTables.makeTome(I18n.get("opotato.arsnouveau.tome.fireball"), (new Spell()).add(MethodProjectile.INSTANCE).add(EffectIgnite.INSTANCE).add(EffectExplosion.INSTANCE).add(AugmentAmplify.INSTANCE, 2).add(AugmentAOE.INSTANCE, 2), I18n.get("opotato.arsnouveau.tome.fireball.flavorText")));
        if (runeOfRenewing.get()) RARE_LOOT.add(() -> LootTables.makeTome(I18n.get("opotato.arsnouveau.tome.rune_of_renewing"), (new Spell()).add(MethodTouch.INSTANCE).add(EffectRune.INSTANCE).add(EffectDispel.INSTANCE).add(EffectHeal.INSTANCE).add(AugmentExtendTime.INSTANCE), I18n.get("opotato.arsnouveau.tome.rune_of_renewing.flavorText")));
        if (knockedOutOfOrbit.get()) RARE_LOOT.add(() -> LootTables.makeTome(I18n.get("opotato.arsnouveau.tome.knocked_out_of_orbit"), (new Spell()).add(MethodOrbit.INSTANCE).add(EffectLaunch.INSTANCE).add(AugmentAmplify.INSTANCE, 2).add(EffectDelay.INSTANCE).add(EffectKnockback.INSTANCE).add(AugmentAmplify.INSTANCE, 2), I18n.get("opotato.arsnouveau.tome.knocked_out_of_orbit.flavorText")));
        if (takeoff.get()) RARE_LOOT.add(() -> LootTables.makeTome(I18n.get("opotato.arsnouveau.tome.takeoff"), (new Spell()).add(MethodSelf.INSTANCE).add(EffectLaunch.INSTANCE, 2).add(EffectGlide.INSTANCE).add(AugmentDurationDown.INSTANCE), I18n.get("opotato.arsnouveau.tome.takeoff.flavorText")));
    }
}

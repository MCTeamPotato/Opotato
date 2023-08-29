package com.teampotato.opotato.config;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class CataclysmExtraConfig {
    public static final ForgeConfigSpec cataclysmExtraConfig;

    public static final ForgeConfigSpec.BooleanValue finalFractalCanBeDamaged;
    public static final ForgeConfigSpec.BooleanValue ignitiumArmorCanBeDamaged;
    public static final ForgeConfigSpec.BooleanValue infernalForgeCanBeDamaged;
    public static final ForgeConfigSpec.BooleanValue monstrousHelmetCanBeDamaged;
    public static final ForgeConfigSpec.BooleanValue incineratorCanBeDamaged;
    public static final ForgeConfigSpec.BooleanValue soulBlackSmithTransformSurroundingLand;
    public static final ForgeConfigSpec.BooleanValue ruinedCitadelTransformSurroundingLand;
    public static final ForgeConfigSpec.BooleanValue burningArenaTransformSurroundingLand;
    public static final ForgeConfigSpec.IntValue soulBlackSmithSpacing;
    public static final ForgeConfigSpec.IntValue soulBlackSmithSeparation;
    public static final ForgeConfigSpec.IntValue ruinedCitadelSpacing;
    public static final ForgeConfigSpec.IntValue ruinedCitadelSeparation;
    public static final ForgeConfigSpec.IntValue burningArenaSpacing;
    public static final ForgeConfigSpec.IntValue burningArenaSeparation;
    public static final ForgeConfigSpec.LongValue soulBlackSmithSalt;
    public static final ForgeConfigSpec.LongValue ruinedCitadelSalt;
    public static final ForgeConfigSpec.LongValue burningArenaSalt;
    public static final ForgeConfigSpec.DoubleValue flameStrikeSoulDamage;
    public static final ForgeConfigSpec.DoubleValue flameStrikeNormalDamage;
    public static final ForgeConfigSpec.DoubleValue infernalForgeEarthQuakeRadius;
    public static final ForgeConfigSpec.DoubleValue flameStrikeNormalPercentageDamage;
    public static final ForgeConfigSpec.DoubleValue flameStrikePlayerPercentageDamage;
    public static final ForgeConfigSpec.IntValue blazingBrandDurationOnFlameStrike;
    public static final ForgeConfigSpec.IntValue infernalForgeCoolDown;
    public static final ForgeConfigSpec.IntValue infernalForgeFireDuration;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> finalFractalValidRepairItem;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> monstrousHelmetValidRepairItem;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> zweienderHelmetValidRepairItem;
    public static final ForgeConfigSpec.BooleanValue canInfernalForgeDisableShield;
    public static final ForgeConfigSpec.IntValue infernalForgeEnchantmentValue;
    public static final ForgeConfigSpec.DoubleValue infernalForgeAttackKnockBack;
    public static final ForgeConfigSpec.IntValue monstrousHelmetCoolDown;
    public static final ForgeConfigSpec.DoubleValue monstrousHelmetExplosionRadius, monstrousHelmetKnockBackRadius;
    public static final ForgeConfigSpec.ConfigValue<String> monstrousHelmetExplosionBlockInteraction;
    public static final ForgeConfigSpec.IntValue monstrousHelmetMonstrousEffectDuration;
    public static final ForgeConfigSpec.IntValue incineratorDurability;
    public static final ForgeConfigSpec.IntValue incineratorChargeTicks;
    public static final ForgeConfigSpec.ConfigValue<String> incineratorAnimationWhenCharging;
    public static final ForgeConfigSpec.IntValue incineratorEnchantmentValue;
    public static final ForgeConfigSpec.IntValue incineratorCoolDownTicks;
    public static final ForgeConfigSpec.IntValue flameStrikeSummonedByIncineratorDuration;
    public static final ForgeConfigSpec.DoubleValue flameStrikeSummonedByIncineratorRadius;
    public static final ForgeConfigSpec.BooleanValue showStatusMessageWhenIncineratorFlameStrikeIsReady;
    public static final ForgeConfigSpec.IntValue voidCoreCoolDown;
    public static final ForgeConfigSpec.BooleanValue voidCoreCanBeDamaged;
    public static final ForgeConfigSpec.IntValue voidCoreDurability;
    public static final ForgeConfigSpec.BooleanValue zweienderCanBeDamaged;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("CataclysmExtraConfig");
        builder.push("Entities");
        flameStrikeSummonedByIncineratorRadius = builder.defineInRange("flameStrikeSummonedByIncineratorRadius", 1.00, 0.10, Double.MAX_VALUE);
        flameStrikeSummonedByIncineratorDuration = builder.defineInRange("flameStrikeSummonedByIncineratorDuration", 40, 1, Integer.MAX_VALUE);
        flameStrikeSoulDamage = builder.defineInRange("flameStrikeSoulDamage", 8.00, 0.00, Double.MAX_VALUE);
        flameStrikeNormalDamage = builder.defineInRange("flameStrikeNormalDamage", 6.00, 0.00, Double.MAX_VALUE);
        flameStrikePlayerPercentageDamage = builder.defineInRange("flameStrikePlayerPercentageDamage", 0.02, 0.00, Double.MAX_VALUE);
        flameStrikeNormalPercentageDamage = builder.defineInRange("flameStrikePercentageDamage", 0.06, 0.00, Double.MAX_VALUE);
        blazingBrandDurationOnFlameStrike = builder.defineInRange("blazingBrandDurationOnFlameStrike", 200, 1, Integer.MAX_VALUE);
        builder.pop();
        builder.push("Items");
        builder.push("Zweiender");
        zweienderCanBeDamaged = builder.define("zweienderCanBeDamaged", false);
        zweienderHelmetValidRepairItem = builder.defineList("zweienderHelmetValidRepairItem", new ObjectArrayList<>(), o -> true);
        builder.pop();
        builder.push("VoidCore");
        voidCoreCanBeDamaged = builder.define("voidCoreCanBeDamaged", false);
        voidCoreDurability = builder.defineInRange("voidCoreDurability", 200, 1, Integer.MAX_VALUE);
        voidCoreCoolDown = builder.defineInRange("voidCoreCoolDown", 120, 1, Integer.MAX_VALUE);
        builder.pop();
        builder.push("TheIncinerator");
        incineratorCanBeDamaged = builder.define("incineratorCanBeDamaged", false);
        showStatusMessageWhenIncineratorFlameStrikeIsReady = builder.define("showStatusMessageWhenIncineratorFlameStrikeIsReady", true);
        incineratorCoolDownTicks = builder.defineInRange("incineratorCoolDownTicks", 400, 1, Integer.MAX_VALUE);
        incineratorEnchantmentValue = builder.comment("Take a loot at Minecraft Wiki to learn more about EnchantmentValue (aka Enchantability) based on item tiers (Pay attention to that table.): https://minecraft.fandom.com/wiki/Tiers").defineInRange("incineratorEnchantmentValue", 16, 1, Integer.MAX_VALUE);
        incineratorAnimationWhenCharging = builder.comment("Seven values are allowed: NONE, EAT, DRINK, BLOCK, BOW, SPEAR, CROSSBOW").define("incineratorAnimationWhenCharging", "BOW");
        incineratorChargeTicks = builder.defineInRange("incineratorChargeTicks", 60, 1, Integer.MAX_VALUE);
        incineratorDurability = builder.defineInRange("incineratorDurability", 4096, 1, Integer.MAX_VALUE);
        builder.pop();
        builder.push("InfernalForge");
        canInfernalForgeDisableShield = builder.comment("Turn this on to allow the right-click of Internal Forge to ignore shield in player's offhand.").define("canInfernalForgeDisableShield", true);
        infernalForgeEarthQuakeRadius = builder.comment("Right click Infernal Forge to trigger Earth Quake, and here is its radius.").defineInRange("infernalForgeEarthQuakeRadius", 4.0, 1.0, Double.MAX_VALUE);
        infernalForgeFireDuration = builder.comment("When the player's health is below half of their maximum health, Infernal Forge's attack will set them on fire.").defineInRange("infernalForgeFireSeconds", 5, 1, Integer.MAX_VALUE);
        infernalForgeCanBeDamaged = builder.define("infernalForgeCanBeDamaged", false);
        infernalForgeCoolDown = builder.defineInRange("infernalForgeCoolDown", 80, 1, Integer.MAX_VALUE);
        infernalForgeAttackKnockBack = builder.defineInRange("infernalForgeAttackKnockBackStrength", 1.0, 0.1, Double.MAX_VALUE);
        infernalForgeEnchantmentValue = builder.comment("Take a loot at Minecraft Wiki to learn more about EnchantmentValue (aka Enchantability) based on item tiers (Pay attention to that table.): https://minecraft.fandom.com/wiki/Tiers").defineInRange("infernalForgeEnchantmentValue", 16, 1, Integer.MAX_VALUE);
        builder.pop();
        builder.push("FinalFractal");
        finalFractalCanBeDamaged = builder.define("finalFractalCanBeDamage", false);
        finalFractalValidRepairItem = builder.comment("If you want one item to repair Final Fractal, then write it here.").defineList("finalFractalValidRepairItem", new ObjectArrayList<>(), o -> true);
        builder.pop();
        builder.push("IgnitiumArmor");
        ignitiumArmorCanBeDamaged = builder.define("ignitiumArmorCanbeDamaged", false);
        builder.pop();
        builder.push("MonstrousHelmet");
        monstrousHelmetCanBeDamaged = builder.define("monstrousHelmetCanBeDamaged", false);
        monstrousHelmetMonstrousEffectDuration = builder.defineInRange("monstrousHelmetMonstrousEffectDuration", 200, 1, Integer.MAX_VALUE);
        monstrousHelmetKnockBackRadius = builder.comment("Centered on the player, the entities in this radius will be pushed away").defineInRange("monstrousHelmetKnockBackRadius", 4.0F, 0.1F, Float.MAX_VALUE);
        monstrousHelmetCoolDown = builder.defineInRange("monstrousHelmetCoolDown", 350, 1, Integer.MAX_VALUE);
        monstrousHelmetExplosionBlockInteraction = builder.comment("Three values are allowed: NONE, BREAK, DESTROY", "Any other value will lead to a crash.").define("monstrousHelmetExplosionBlockInteraction", "NONE");
        monstrousHelmetExplosionRadius = builder.defineInRange("monstrousHelmetExplosionAbilityRadius", 4.00, 1.00, Double.MAX_VALUE);
        monstrousHelmetValidRepairItem = builder.comment("If you want one item to repair Monstrous Helm, then write it here.").defineList("monstrousHelmetValidRepairItem", new ObjectArrayList<>(), o -> true);
        builder.pop();
        builder.pop();
        builder.push("Structures");
        builder.push("SoulBlackSmith");
        soulBlackSmithSpacing = builder.defineInRange("soulBlackSmithSpacing", 90, 1, Integer.MAX_VALUE);
        soulBlackSmithSeparation = builder.defineInRange("soulBlackSmithSeparation", 70, 1, Integer.MAX_VALUE);
        soulBlackSmithSalt = builder.defineInRange("soulBlackSmithSalt", 1984567320L, 1L, Long.MAX_VALUE);
        soulBlackSmithTransformSurroundingLand = builder.define("soulBlackSmithTransformSurroundingLand", false);
        builder.pop();
        builder.push("RuinedCitadel");
        ruinedCitadelSpacing = builder.defineInRange("ruinedCitadelSpacing", 30, 1, Integer.MAX_VALUE);
        ruinedCitadelSeparation = builder.defineInRange("ruinedCitadelSeparation", 10, 1, Integer.MAX_VALUE);
        ruinedCitadelSalt = builder.defineInRange("ruinedCitadelSalt", 367895146L, 1L, Long.MAX_VALUE);
        ruinedCitadelTransformSurroundingLand = builder.define("ruinedCitadelTransformSurroundingLand", false);
        builder.pop();
        builder.push("BurningArena");
        burningArenaSpacing = builder.defineInRange("burningArenaSpacing", 112, 1, Integer.MAX_VALUE);
        burningArenaSeparation = builder.defineInRange("burningArenaSeparation", 70, 1, Integer.MAX_VALUE);
        burningArenaSalt = builder.defineInRange("burningArenaSalt", 1923456789L, 1L, Long.MAX_VALUE);
        burningArenaTransformSurroundingLand = builder.define("burningArenaTransformSurroundingLand", false);
        builder.pop();
        builder.pop();
        builder.pop();
        cataclysmExtraConfig = builder.build();
    }
}

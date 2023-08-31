package com.teampotato.opotato.config.mods;

import net.minecraftforge.common.ForgeConfigSpec;

public class FlowingAgonyExtraConfig {
    public static final ForgeConfigSpec flowingAgonyConfig;
    public static final ForgeConfigSpec.DoubleValue commonUndeadDamageReductionPercent, rareUndeadDamageReductionPercent, witherDamageReductionPercent, fungalSpreadingRadius, fungalSpreadingHeight, maxSuccessfulInfectionProbability, fungalInfectionEffectDuration, maxFallDamageReduction, maxExplosionDamageReduction, maxFireDamageReduction, lighthurnFungalInfectionHurtDamageAmounts, burialObjectDetectionRadius, burialObjectDetectionHeight, originalSinErosionMinDamageReduction, originalSinErosionBaseBonus, originalSinErosionBonusInterval, prototypeChaoticTypeBetaExtraDurationBonus, prototypeChaoticTypeBetaWithProtoTypeChaoticExtraDurationBonus, scholarOfOriginalSinMinPlayerHurtBonusPercentage, scholarOfOriginalSinPlayerHurtBonusPerLevel, scholarOfOriginalSinMaxPlayerHurtBonus, scholarOfOriginalSinMinNegativeEffectDurationBonus, scholarOfOriginalSinNegativeEffectDurationBonusPerLevel, scholarOfOriginalSinMaxExtraExp, scholarOfOriginalSinExtraExpBonusPerLevel;
    public static final ForgeConfigSpec.IntValue lighthurnFungalInfectionHurtInterval, prototypeChaoticMaxHealthBonus, prototypeChaoticMaxHealthBonusDuration, prototypeChaoticPerHealthBonus, shadowBornNightVisionDuration, shadowBornLightLevelOnNightVision, shadowBornLightLevelOnImmuningToBlindness;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("FlowingAgonyExtraConfig");
        builder.push("PotionEffects");
        lighthurnFungalInfectionHurtInterval = builder.defineInRange("lighthurnFungalInfectionHurtIntervallTicks", 40, 0, Integer.MAX_VALUE);
        lighthurnFungalInfectionHurtDamageAmounts = builder.defineInRange("lighthurnFungalInfectionHurtDamageAmounts", 3.00, 0.00, Double.MAX_VALUE);
        builder.pop();
        builder.push("TheMistakens");
        builder.push("CorruptedKindred");
        commonUndeadDamageReductionPercent = builder.defineInRange("commonUndeadDamageReductionPercent", 0.50, 0.00, Double.MAX_VALUE);
        rareUndeadDamageReductionPercent = builder.defineInRange("rareUndeadDamageReductionPercent", 0.10, 0.00, Double.MAX_VALUE);
        witherDamageReductionPercent = builder.defineInRange("witherDamageReductionPercent", 0.05F, 0.00, Double.MAX_VALUE);
        builder.pop();
        builder.push("LightburnFungalParasitic");
        fungalSpreadingRadius = builder.defineInRange("fungalSpreadingRadius", 8.00, 0.10, Double.MAX_VALUE);
        fungalSpreadingHeight = builder.defineInRange("fungalSpreadingHeight", 2.00, 0.10, Double.MAX_VALUE);
        maxSuccessfulInfectionProbability = builder.defineInRange("maxSuccessfulInfectionProbability", 0.5, 0.00, Double.MAX_VALUE);
        fungalInfectionEffectDuration = builder.defineInRange("fungalInfectionEffectDuration", 120.00, 1.00, Double.MAX_VALUE);
        maxFallDamageReduction = builder.defineInRange("maxFallDamageReduction", 0.20, 0.00, 1.00);
        maxExplosionDamageReduction = builder.defineInRange("maxExplosionDamageReduction", 0.20, 0.00, 1.00);
        maxFireDamageReduction = builder.defineInRange("maxFireDamageReduction", 0.20, 0.00, 1.00);
        builder.pop();
        builder.push("BurialObject");
        burialObjectDetectionRadius = builder.defineInRange("detectionRadius", 16.00, 0.00, Double.MAX_VALUE);
        burialObjectDetectionHeight = builder.defineInRange("detectionHeight", 1.00, 0.00, Double.MAX_VALUE);
        builder.pop();
        builder.push("OriginalSinErosion");
        originalSinErosionMinDamageReduction = builder.defineInRange("minDamageReduction", 2.00, 0.00, Double.MAX_VALUE);
        builder.comment("This enchantment will increase the exp you received. The increased exp = originalExpYouGot + baseBonus + (enchantmentLevel * bonusInterval)");
        originalSinErosionBaseBonus = builder.defineInRange("baseBonus", 0.05, 0.00, Double.MAX_VALUE);
        originalSinErosionBonusInterval = builder.defineInRange("bonusIntervalBasedOnLevel", 0.05, 0.00, Double.MAX_VALUE);
        builder.pop();
        builder.push("PrototypeChaotic");
        prototypeChaoticMaxHealthBonus = builder.defineInRange("maxHealthBonus", 30, 0, Integer.MAX_VALUE);
        prototypeChaoticMaxHealthBonusDuration = builder.defineInRange("maxHealthBonusDurationTicks", 1200, 1, Integer.MAX_VALUE);
        prototypeChaoticPerHealthBonus = builder.defineInRange("healthBonusPerLevel", 1, 0, Integer.MAX_VALUE);
        prototypeChaoticTypeBetaExtraDurationBonus = builder.defineInRange("extraDurationBonus", 1.00, 0.00, Double.MAX_VALUE);
        prototypeChaoticTypeBetaWithProtoTypeChaoticExtraDurationBonus = builder.defineInRange("withPrototypeChaoticExtraDurationBonus", 2.00, 0.00, Double.MAX_VALUE);
        builder.pop();
        builder.push("ScholarOfOriginalSin");
        scholarOfOriginalSinMinPlayerHurtBonusPercentage = builder.defineInRange("minPlayerHurtBonusPercentage", 0.80, 0.00, 1.00);
        scholarOfOriginalSinPlayerHurtBonusPerLevel = builder.defineInRange("playerHurtBonusPerLevel", 0.10, 0.00, 1.00);
        scholarOfOriginalSinMaxPlayerHurtBonus = builder.defineInRange("maxPlayerHurtBonus",10.00, 0.00, Double.MAX_VALUE);
        scholarOfOriginalSinMinNegativeEffectDurationBonus = builder.defineInRange("minNegativeEffectDurationBonus", 0.80, 0.00, Double.MAX_VALUE);
        scholarOfOriginalSinNegativeEffectDurationBonusPerLevel = builder.defineInRange("scholarOfOriginalSinNegativeEffectDurationBonusPerLevel", 0.10, 0.00, Double.MAX_VALUE);
        scholarOfOriginalSinMaxExtraExp = builder.defineInRange("scholarOfOriginalSinMaxExtraExp", 0.80, 0.00, Double.MAX_VALUE);
        scholarOfOriginalSinExtraExpBonusPerLevel = builder.defineInRange("scholarOfOriginalSinExtraExpBonusPerLevel", 0.15, 0.0, Double.MAX_VALUE);
        builder.pop();
        builder.push("Shadowborn");
        shadowBornNightVisionDuration = builder.defineInRange("shadowBornNightVisionDuration", 1200, 1, Integer.MAX_VALUE);
        shadowBornLightLevelOnNightVision = builder.defineInRange("shadowBornLightLevelOnNightVision", 5, 0, Integer.MAX_VALUE);
        shadowBornLightLevelOnImmuningToBlindness = builder.defineInRange("shadowBornLightLevelOnImmuningToBlindness", 5, 0, Integer.MAX_VALUE);
        builder.pop();
        builder.pop();
        builder.pop();
        flowingAgonyConfig = builder.build();
    }
}

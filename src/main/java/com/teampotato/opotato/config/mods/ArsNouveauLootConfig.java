package com.teampotato.opotato.config.mods;

import net.minecraftforge.common.ForgeConfigSpec;

public class ArsNouveauLootConfig {
    public static final ForgeConfigSpec arsNouveauConfig;

    public static final ForgeConfigSpec.BooleanValue manaGem;

    public static final ForgeConfigSpec.BooleanValue wildenHorn;
    public static final ForgeConfigSpec.BooleanValue wildenSpike;
    public static final ForgeConfigSpec.BooleanValue wildenWing;
    public static final ForgeConfigSpec.BooleanValue manaBerryBush;
    public static final ForgeConfigSpec.BooleanValue longManaRegenPotion;
    public static final ForgeConfigSpec.BooleanValue strongManaRegenPotion;
    public static final ForgeConfigSpec.BooleanValue manaRegenPotion;
    public static final ForgeConfigSpec.IntValue manaGemMaxCount;
    public static final ForgeConfigSpec.IntValue manaGemBasicCount;
    public static final ForgeConfigSpec.IntValue wildenHornMaxCount;
    public static final ForgeConfigSpec.IntValue wildenHornBasicCount;
    public static final ForgeConfigSpec.IntValue wildenSpikeMaxCount;
    public static final ForgeConfigSpec.IntValue wildenSpikeBasicCount;
    public static final ForgeConfigSpec.IntValue wildenWingMaxCount;
    public static final ForgeConfigSpec.IntValue wildenWingBasicCount;
    public static final ForgeConfigSpec.IntValue manaBerryBushMaxCount;
    public static final ForgeConfigSpec.IntValue manaBerryBushBasicCount;

    public static final ForgeConfigSpec.BooleanValue warpScroll;
    public static final ForgeConfigSpec.BooleanValue carbuncleShard;
    public static final ForgeConfigSpec.BooleanValue sylphShard;
    public static final ForgeConfigSpec.BooleanValue drygmyShard;
    public static final ForgeConfigSpec.BooleanValue wixieShard;
    public static final ForgeConfigSpec.BooleanValue amplifyArrow;
    public static final ForgeConfigSpec.BooleanValue splitArrow;
    public static final ForgeConfigSpec.BooleanValue pierceArrow;
    public static final ForgeConfigSpec.BooleanValue ritualTablets;

    public static final ForgeConfigSpec.IntValue warpScrollBasicCount;
    public static final ForgeConfigSpec.IntValue warpScrollMaxCount;
    public static final ForgeConfigSpec.IntValue amplifyArrowBasicCount;
    public static final ForgeConfigSpec.IntValue amplifyArrowMaxCount;
    public static final ForgeConfigSpec.IntValue splitArrowBasicCount;
    public static final ForgeConfigSpec.IntValue splitArrowMaxCount;
    public static final ForgeConfigSpec.IntValue pierceArrowBasicCount;
    public static final ForgeConfigSpec.IntValue pierceArrowMaxCount;

    public static final ForgeConfigSpec.BooleanValue xacrisTinyHut, glowTrap, baileysBovineRocket, arachnesWeaving, warpImpact, farfallasFrostyFlames, gootasticsTelekineticFishingRod, potentToxin, theShadowsTemporaryTunnel, vault, fireball, runeOfRenewing, knockedOutOfOrbit, takeoff;


    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("ArsNouveauLootConfig");
        builder.comment("Part of ArsNouveau loot data was hard-coded, but now you can tweak them here",
                "TheNumberOfItems = basicCount + aRandomNumberBetweenZeroAndMaxCount",
                "Another example, if you turn 'manaGem' off, this items will not appear in loots anymore.");
        builder.push("BasicLoot");
        manaGem = builder.define("manaGem", true);
        manaGemBasicCount = builder.defineInRange("manaGemBasicCount", 1, 0, 64);
        manaGemMaxCount = builder.defineInRange("manaGemMaxCount", 5, 1, 64);
        wildenHorn = builder.define("wildenHorn", true);
        wildenHornBasicCount = builder.defineInRange("wildenHornBasicCount", 1, 0, 64);
        wildenHornMaxCount = builder.defineInRange("wildenHornMaxCount", 3, 1, 64);
        wildenSpike = builder.define("wildenSpike", true);
        wildenSpikeBasicCount = builder.defineInRange("wildenSpikeBasicCount", 1, 0, 64);
        wildenSpikeMaxCount = builder.defineInRange("wildenSpikeMaxCount", 3, 1, 64);
        wildenWing = builder.define("wildenWing", true);
        wildenWingBasicCount = builder.defineInRange("wildenWingBasicCount", 1, 0, 64);
        wildenWingMaxCount = builder.defineInRange("wildenWingMaxCount", 3, 1, 64);
        manaBerryBush = builder.define("manaBerryBush", true);
        manaBerryBushBasicCount = builder.defineInRange("manaBerryBushBasicCount", 1, 0, 64);
        manaBerryBushMaxCount = builder.defineInRange("manaBerryBushMaxCount", 3, 1, 64);
        longManaRegenPotion = builder.define("longManaRegenPotion", true);
        strongManaRegenPotion = builder.define("strongManaRegenPotion", true);
        manaRegenPotion = builder.define("manaRegenPotion", true);
        builder.pop();
        builder.push("UncommonLoot");
        warpScroll = builder.define("warpScroll", true);
        warpScrollBasicCount = builder.defineInRange("warpScrollBasicCount", 1, 0, 64);
        warpScrollMaxCount = builder.defineInRange("warpScrollMaxCount", 2, 1, 64);
        carbuncleShard = builder.define("carbuncleShard", true);
        sylphShard = builder.define("sylphShard", true);
        drygmyShard = builder.define("drygmyShard", true);
        wixieShard = builder.define("wixieShard", true);
        amplifyArrow = builder.define("amplifyArrow", true);
        amplifyArrowBasicCount = builder.defineInRange("amplifyArrowBasicCount", 16, 0, 64);
        amplifyArrowMaxCount = builder.defineInRange("amplifyArrowMaxCount", 16, 0, 64);
        splitArrow = builder.define("splitArrow", true);
        splitArrowBasicCount = builder.defineInRange("splitArrowBasicCount", 16, 0, 64);
        splitArrowMaxCount = builder.defineInRange("splitArrowMaxCount", 16, 0, 64);
        pierceArrow = builder.define("pierceArrow", true);
        pierceArrowBasicCount = builder.defineInRange("pierceArrowBasicCount", 16, 0, 64);
        pierceArrowMaxCount = builder.defineInRange("pierceArrowMaxCount", 16, 0, 64);
        ritualTablets = builder.define("ritualTablets", true);
        builder.pop();
        builder.push("RareLoot");
        xacrisTinyHut = builder.define("xacrisTinyHut", true);
        glowTrap = builder.define("glowTrap", true);
        baileysBovineRocket = builder.define("baileysBovineRocket", true);
        arachnesWeaving = builder.define("arachnesWeaving", true);
        warpImpact = builder.define("warpImpact", true);
        farfallasFrostyFlames = builder.define("farfallasFrostyFlames", true);
        gootasticsTelekineticFishingRod = builder.define("gootasticsTelekineticFishingRod", true);
        potentToxin = builder.define("potentToxin", true);
        theShadowsTemporaryTunnel = builder.define("theShadowsTemporaryTunnel", true);
        fireball = builder.define("fireball", true);
        vault = builder.define("vault", true);
        runeOfRenewing = builder.define("runeOfRenewing", true);
        knockedOutOfOrbit = builder.define("knockedOutOfOrbit", true);
        takeoff = builder.define("takeoff", true);
        builder.pop();
        builder.pop();
        arsNouveauConfig = builder.build();
    }
}

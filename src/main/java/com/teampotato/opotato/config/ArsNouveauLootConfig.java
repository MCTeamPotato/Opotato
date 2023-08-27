package com.teampotato.opotato.config;

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
    public static final ForgeConfigSpec.IntValue wildenHornMaxCount;
    public static final ForgeConfigSpec.IntValue wildenSpikeMaxCount;
    public static final ForgeConfigSpec.IntValue wildenWingMaxCount;
    public static final ForgeConfigSpec.IntValue manaBerryBushMaxCount;

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

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("ArsNouveauLootConfig");
        builder.comment("Part of ArsNouveau loot data was hard-coded, but now you can tweak them here",
                "The maxCount value is exclusive, and the basicCount value means that the items will at least be more than it.",
                "### BasicLoot");
        manaGem = builder.define("manaGem", true);
        manaGemMaxCount = builder.defineInRange("manaGemMaxCount", 5, 1, 64);
        wildenHorn = builder.define("wildenHorn", true);
        wildenHornMaxCount = builder.defineInRange("wildenHornMaxCount", 3, 1, 64);
        wildenSpike = builder.define("wildenSpike", true);
        wildenSpikeMaxCount = builder.defineInRange("wildenSpikeMaxCount", 3, 1, 64);
        wildenWing = builder.define("wildenWing", true);
        wildenWingMaxCount = builder.defineInRange("wildenWingMaxCount", 3, 1, 64);
        manaBerryBush = builder.define("manaBerryBush", true);
        manaBerryBushMaxCount = builder.defineInRange("manaBerryBushMaxCount", 3, 1, 64);
        longManaRegenPotion = builder.define("longManaRegenPotion", true);
        strongManaRegenPotion = builder.define("strongManaRegenPotion", true);
        manaRegenPotion = builder.define("manaRegenPotion", true);
        builder.comment("### UncommonLoot");
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
        arsNouveauConfig = builder.build();
    }
}

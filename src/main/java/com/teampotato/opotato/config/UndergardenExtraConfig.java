package com.teampotato.opotato.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class UndergardenExtraConfig {
    public static final ForgeConfigSpec undergardenConfig;
    public static final ForgeConfigSpec.IntValue catacombSpacing;
    public static final ForgeConfigSpec.IntValue catacombSeparation;
    public static final ForgeConfigSpec.IntValue catacombSalt;

    public static final ForgeConfigSpec.BooleanValue catacombTransformSurroundingLand;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("UndergardenExtraConfig");
        catacombSpacing = builder.defineInRange("catacombSpacing", 24, 1, Integer.MAX_VALUE);
        catacombSeparation = builder.defineInRange("catacombSeparation", 8, 1, Integer.MAX_VALUE);
        catacombSalt = builder.defineInRange("catacombSalt", 276320045, 1, Integer.MAX_VALUE);
        catacombTransformSurroundingLand = builder.define("catacombTransformSurroundingLand", true);
        builder.pop();
        undergardenConfig = builder.build();
    }
}

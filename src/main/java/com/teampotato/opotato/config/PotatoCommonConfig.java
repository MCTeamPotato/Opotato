package com.teampotato.opotato.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class PotatoCommonConfig {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.BooleanValue ENABLE_BLUE_SKIES_NERF, ALLOW_EVERY_MOD_GEN_FEATURE_IN_DIM;
    public static ForgeConfigSpec.IntValue GATE_KEEPER_HOUSE_SPACING;
    static {
        ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();
        CONFIG_BUILDER.comment("Opotato Common Config").push("Blue Skies Extra Settings");
        ENABLE_BLUE_SKIES_NERF = CONFIG_BUILDER.define("enable blue skies nerf", true);
        ALLOW_EVERY_MOD_GEN_FEATURE_IN_DIM = CONFIG_BUILDER.define("allow every mod generate features in blueskies dimensions", false);
        GATE_KEEPER_HOUSE_SPACING = CONFIG_BUILDER.defineInRange("gate keeper house spacing", 18, 6, Integer.MAX_VALUE);
        CONFIG_BUILDER.pop();
        CONFIG_BUILDER.push("Epic Fight Forge Version Detection");
        COMMON_CONFIG = CONFIG_BUILDER.build();
    }
}

package com.teampotato.opotato.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class PotatoCommonConfig {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.BooleanValue ENABLE_BLUE_SKIES_NERF, ALLOW_EVERY_MOD_GEN_FEATURE_IN_DIM, ENABLE_HEADSHOT_SOUND_DING, KILL_WITHER_STORM_MOD_ENTITIES_AFTER_COMMAND_BLOCK_DIES;
    public static ForgeConfigSpec.IntValue GATE_KEEPER_HOUSE_SPACING;
    public static ForgeConfigSpec.ConfigValue<Float> HEADSHOT_VOLUME, HEADSHOT_PITCH;
    static {
        ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();
        CONFIG_BUILDER.comment("Opotato Common Config").push("Blue Skies Extra Settings");
        ENABLE_BLUE_SKIES_NERF = CONFIG_BUILDER
                .define("enable blue skies nerf", true);
        ALLOW_EVERY_MOD_GEN_FEATURE_IN_DIM = CONFIG_BUILDER
                .define("allow every mod generate features in blueskies dimensions", false);
        GATE_KEEPER_HOUSE_SPACING = CONFIG_BUILDER
                .defineInRange("gate keeper house spacing", 18, 6, Integer.MAX_VALUE);
        CONFIG_BUILDER.pop();
        CONFIG_BUILDER.push("Headshot Extra Settings");
        ENABLE_HEADSHOT_SOUND_DING = CONFIG_BUILDER
                .define("enable \"Ding\" on Headshot", true);
        HEADSHOT_VOLUME = CONFIG_BUILDER
                .comment("Range: 0.0F ~ 1.0F. Type: Float")
                .define("\"Ding\" volume", 1.0F);
        HEADSHOT_PITCH = CONFIG_BUILDER
                .comment("Range: 0.5F ~ 2.0F. Type: Float")
                .define("\"Ding\" pitch", 1.0F);
        CONFIG_BUILDER.pop();
        CONFIG_BUILDER.push("Cracker's Wither Storm Mod Optimization Settings");
        KILL_WITHER_STORM_MOD_ENTITIES_AFTER_COMMAND_BLOCK_DIES = CONFIG_BUILDER
                .comment("Entities that will be killed: Block Cluster, Sickened Skeleton, Sickened Creeper, Sickened Spider, Sickened Zombie, Tentacle, Withered Symbiont.")
                .comment("Inspired by Lag Removal mod")
                .define("kill witherstormmod entities after command block dies", false);
        COMMON_CONFIG = CONFIG_BUILDER.build();
    }
}

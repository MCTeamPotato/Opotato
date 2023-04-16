package com.teampotato.opotato.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class PotatoCommonConfig {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.BooleanValue ALLOW_LIMIT_MAX_SPAWN, ENABLE_BLUE_SKIES_NERF, ALLOW_EVERY_MOD_GEN_FEATURE_IN_DIM, ENABLE_HEADSHOT_SOUND_DING, KILL_WITHER_STORM_MOD_ENTITIES_AFTER_COMMAND_BLOCK_DIES, PRINT_MOD_LIST_WHEN_LAUNCHING_GAME;
    public static ForgeConfigSpec.IntValue GATE_KEEPER_HOUSE_SPACING, MAX_ENTITIES_NUMBER_PER_CHUNK, SOUL_BLACK_SMITH_SPACING, SOUL_BLACK_SMITH_SEPARATION, RUINED_CITADEL_SPACING, RUINED_CITADEL_SEPARATION, BURNING_ARENA_SPACING, BURNING_ARENA_SEPARATION, CATACOMB_SPACING, CATACOMB_SEPARATION;
    public static ForgeConfigSpec.ConfigValue<Float> HEADSHOT_VOLUME, HEADSHOT_PITCH;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BLACKLIST;

    static {
        ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();
        CONFIG_BUILDER.comment("Opotato Common Config");

        CONFIG_BUILDER.push("Opotato Custom Optimization Settings");
        PRINT_MOD_LIST_WHEN_LAUNCHING_GAME = CONFIG_BUILDER.define("print mods list when launching game", true);
        ALLOW_LIMIT_MAX_SPAWN = CONFIG_BUILDER.define("allow entities spawn limit", false);
        MAX_ENTITIES_NUMBER_PER_CHUNK = CONFIG_BUILDER.defineInRange("max entities spawning number per chunk", 10, 1, 100);
        BLACKLIST = CONFIG_BUILDER.defineList("entities whose spawn won't be limited", Lists.newArrayList("modid:mobid"), o -> o instanceof String);
        CONFIG_BUILDER.pop();
        
        CONFIG_BUILDER.push("Blue Skies Extra Settings");
        ENABLE_BLUE_SKIES_NERF = CONFIG_BUILDER.define("enable blue skies nerf", true);
        ALLOW_EVERY_MOD_GEN_FEATURE_IN_DIM = CONFIG_BUILDER.define("allow every mod generate features in blueskies dimensions", false);
        GATE_KEEPER_HOUSE_SPACING = CONFIG_BUILDER.defineInRange("gate keeper house spacing", 18, 6, Integer.MAX_VALUE);
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER.push("Headshot Extra Settings");
        ENABLE_HEADSHOT_SOUND_DING = CONFIG_BUILDER.define("enable 'Ding' on Headshot", true);
        HEADSHOT_VOLUME = CONFIG_BUILDER.comment("Range: 0.0F ~ 1.0F. Type: Float").define("'Ding' volume", 1.0F);
        HEADSHOT_PITCH = CONFIG_BUILDER.comment("Range: 0.5F ~ 2.0F. Type: Float").define("'Ding' pitch", 1.0F);
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER.push("Cracker's Wither Storm Mod Optimization Settings");
        KILL_WITHER_STORM_MOD_ENTITIES_AFTER_COMMAND_BLOCK_DIES = CONFIG_BUILDER.comment("Entities that will be killed: Block Cluster, Sickened Skeleton, Sickened Creeper, Sickened Spider, Sickened Zombie, Tentacle, Withered Symbiont.", "Inspired by Lag Removal mod").define("kill witherstormmod entities after command block dies", false);
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER.push("L_Ender's Cataclysm Extra Settings");
        SOUL_BLACK_SMITH_SPACING = CONFIG_BUILDER.defineInRange("soul black smith spacing", 90, 5, Integer.MAX_VALUE);
        SOUL_BLACK_SMITH_SEPARATION = CONFIG_BUILDER.defineInRange("soul black smith separation", 70, 5, Integer.MAX_VALUE);
        RUINED_CITADEL_SPACING = CONFIG_BUILDER.defineInRange("ruined citadel spacing", 30, 5, Integer.MAX_VALUE);
        RUINED_CITADEL_SEPARATION = CONFIG_BUILDER.defineInRange("ruined citadel separation", 10, 5, Integer.MAX_VALUE);
        BURNING_ARENA_SPACING = CONFIG_BUILDER.defineInRange("burning arena spacing", 112, 5, Integer.MAX_VALUE);
        BURNING_ARENA_SEPARATION = CONFIG_BUILDER.defineInRange("burning arena separation", 70, 5, Integer.MAX_VALUE);
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER.push("The Undergarden Extra Settings");
        CATACOMB_SPACING = CONFIG_BUILDER.defineInRange("catacomb spacing", 24, 5, Integer.MAX_VALUE);
        CATACOMB_SEPARATION = CONFIG_BUILDER.defineInRange("catacomb separation", 8, 5, Integer.MAX_VALUE);
        CONFIG_BUILDER.pop();

        COMMON_CONFIG = CONFIG_BUILDER.build();
    }
}

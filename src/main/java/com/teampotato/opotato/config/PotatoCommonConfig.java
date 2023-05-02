package com.teampotato.opotato.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class PotatoCommonConfig {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.BooleanValue ALLOW_LIMIT_MAX_SPAWN, KILL_WITHER_STORM_MOD_ENTITIES_AFTER_COMMAND_BLOCK_DIES, LET_WITHER_SICKNESS_ONLY_TAKE_EFFECT_ON_PLAYERS, LET_MOBS_IMMUNE_TO_WITHER_SICKNESS_TICKING_SYSTEM, LET_ANIMALS_IMMUNE_TO_WITHER_SICKNESS_TICKING_SYSTEM, BLOCK_CLUSTER_RENDER_OPTIMIZATION, REDUCE_THE_WITHER_STORM_CHUNK_ACTIVITY, ENABLE_CREATIVE_ONE_POUCH;
    public static ForgeConfigSpec.IntValue MAX_ENTITIES_NUMBER_PER_CHUNK;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BLACKLIST;

    static {
        ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();
        CONFIG_BUILDER.comment("Opotato Common Config");

        CONFIG_BUILDER.push("Opotato Settings");
        ALLOW_LIMIT_MAX_SPAWN = CONFIG_BUILDER
                .define("allow entities spawn limit", false);
        MAX_ENTITIES_NUMBER_PER_CHUNK = CONFIG_BUILDER
                .defineInRange("max entities spawning number per chunk", 10, 1, 100);
        BLACKLIST = CONFIG_BUILDER
                .defineList("entities whose spawn won't be limited", Lists.newArrayList("modid:mobid"), o -> o instanceof String);
        ENABLE_CREATIVE_ONE_POUCH = CONFIG_BUILDER
                .define("enable creative one pouch", true);
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER.push("Cracker's Wither Storm Mod Optimization Settings")
        .comment("Every settings here will improve performance a lot as the mod is really laggy");
        LET_WITHER_SICKNESS_ONLY_TAKE_EFFECT_ON_PLAYERS = CONFIG_BUILDER
                .define("let wither sickness ticking system only take effect on players, NOT AVAILABLE YET, DON'T ENABLE THIS", false);
        LET_MOBS_IMMUNE_TO_WITHER_SICKNESS_TICKING_SYSTEM = CONFIG_BUILDER
                .define("don't let the wither sickness ticking system affect monsters, NOT AVAILABLE YET, DON'T ENABLE THIS", false);
        LET_ANIMALS_IMMUNE_TO_WITHER_SICKNESS_TICKING_SYSTEM = CONFIG_BUILDER
                .define("don't let the wither sickness ticking system affect animals, NOT AVAILABLE YET, DON'T ENABLE THIS", false);
        BLOCK_CLUSTER_RENDER_OPTIMIZATION = CONFIG_BUILDER
                .comment("With this turning on, the amount of block clusters will be reduced on rendering and they won't be rendered when no player see them, NOT AVAILABLE YET")
                .define("block cluster render optimization", false);
        REDUCE_THE_WITHER_STORM_CHUNK_ACTIVITY = CONFIG_BUILDER
                .define("reduce the wither storm chunks activity by one half, NOT AVAILABLE YET, DON'T ENABLE THIS", false);
        KILL_WITHER_STORM_MOD_ENTITIES_AFTER_COMMAND_BLOCK_DIES = CONFIG_BUILDER
                .comment("Entities that will be killed: Block Cluster, Sickened Skeleton, Sickened Creeper, Sickened Spider, Sickened Zombie, Tentacle, Withered Symbiont.",
                        "Inspired by Lag Removal mod.")
                .define("kill witherstormmod entities after command block dies", false);
        CONFIG_BUILDER.pop();


        COMMON_CONFIG = CONFIG_BUILDER.build();
    }
}

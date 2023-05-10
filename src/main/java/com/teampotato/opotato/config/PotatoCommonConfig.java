package com.teampotato.opotato.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class PotatoCommonConfig {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.BooleanValue ALLOW_LIMIT_MAX_SPAWN, ENABLE_BLUE_SKIES_NERF, ALLOW_EVERY_MOD_GEN_FEATURE_IN_DIM, ENABLE_HEADSHOT_SOUND_DING, KILL_WITHER_STORM_MOD_ENTITIES_AFTER_COMMAND_BLOCK_DIES, PRINT_MOD_LIST_WHEN_LAUNCHING_GAME, LET_WITHER_SICKNESS_ONLY_TAKE_EFFECT_ON_PLAYERS, LET_MOBS_IMMUNE_TO_WITHER_SICKNESS_TICKING_SYSTEM, LET_ANIMALS_IMMUNE_TO_WITHER_SICKNESS_TICKING_SYSTEM, BLOCK_CLUSTER_RENDER_OPTIMIZATION, ENABLE_CREATIVE_ONE_POUCH, DISABLE_ARS_NOUVEAU_MANA_GEM_IN_BASIC_LOOT, IGNITIUM_ARMOR_HAS_DAMAGE, FINAL_FRACTAL_HAS_DAMAGE, INFERNAL_FORGE_HAS_DAMAGE, MONSTROUS_HELM_HAS_DAMAGE, ZWEIENDER_HAS_DAMAGE;
    public static ForgeConfigSpec.IntValue GATE_KEEPER_HOUSE_SPACING, MAX_ENTITIES_NUMBER_PER_CHUNK, SOUL_BLACK_SMITH_SPACING, SOUL_BLACK_SMITH_SEPARATION, RUINED_CITADEL_SPACING, RUINED_CITADEL_SEPARATION, BURNING_ARENA_SPACING, BURNING_ARENA_SEPARATION, CATACOMB_SPACING, CATACOMB_SEPARATION, INCINERATOR_COOL_DOWN, INFERNAL_FORGE_COOL_DOWN, VOID_CORE_COOL_DOWN;
    public static ForgeConfigSpec.ConfigValue<Float> HEADSHOT_VOLUME, HEADSHOT_PITCH;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BLACKLIST;

    static {
        ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();

        CONFIG_BUILDER
                .comment("Opotato Common Config")
                .push("Opotato Settings");
        PRINT_MOD_LIST_WHEN_LAUNCHING_GAME = CONFIG_BUILDER
                .define("print mods list when launching game", true);
        ALLOW_LIMIT_MAX_SPAWN = CONFIG_BUILDER
                .define("allow entities spawn limit", false);
        MAX_ENTITIES_NUMBER_PER_CHUNK = CONFIG_BUILDER
                .defineInRange("max entities spawning number per chunk", 10, 1, 100);
        BLACKLIST = CONFIG_BUILDER
                .defineList("entities whose spawn won't be limited", Lists.newArrayList("modid:mobid"), o -> o instanceof String);
        ENABLE_CREATIVE_ONE_POUCH = CONFIG_BUILDER
                .define("enable creative one pouch", true);
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER
                .push("Blue Skies Extra Settings");
        ENABLE_BLUE_SKIES_NERF = CONFIG_BUILDER
                .define("enable blue skies nerf", true);
        ALLOW_EVERY_MOD_GEN_FEATURE_IN_DIM = CONFIG_BUILDER
                .define("allow every mod generate features in blue skies dimensions", false);
        GATE_KEEPER_HOUSE_SPACING = CONFIG_BUILDER
                .defineInRange("gate keeper house spacing", 18, 6, Integer.MAX_VALUE);
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER
                .push("Ars Nouveau Extra Settings");
        DISABLE_ARS_NOUVEAU_MANA_GEM_IN_BASIC_LOOT = CONFIG_BUILDER
                .define("disable mana gems generation in basic loot chests", false);
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER
                .push("Headshot Extra Settings");
        ENABLE_HEADSHOT_SOUND_DING = CONFIG_BUILDER
                .define("enable 'Ding' on Headshot", true);
        HEADSHOT_VOLUME = CONFIG_BUILDER
                .comment("Range: 0.0F ~ 1.0F. Type: Float")
                .define("'Ding' volume", 1.0F);
        HEADSHOT_PITCH = CONFIG_BUILDER
                .comment("Range: 0.5F ~ 2.0F. Type: Float")
                .define("'Ding' pitch", 1.0F);
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER
                .push("Cracker's Wither Storm Mod Optimization Settings")
                .comment("Every settings here will improve performance a lot as the mod is really laggy");
        LET_WITHER_SICKNESS_ONLY_TAKE_EFFECT_ON_PLAYERS = CONFIG_BUILDER
                .define("let wither sickness ticking system only take effect on players", false);
        LET_MOBS_IMMUNE_TO_WITHER_SICKNESS_TICKING_SYSTEM = CONFIG_BUILDER
                .define("don't let the wither sickness ticking system affect monsters", false);
        LET_ANIMALS_IMMUNE_TO_WITHER_SICKNESS_TICKING_SYSTEM = CONFIG_BUILDER
                .define("don't let the wither sickness ticking system affect animals", false);
        BLOCK_CLUSTER_RENDER_OPTIMIZATION = CONFIG_BUILDER
                .comment("With this turning on, the amount of block clusters will be reduced on rendering and they won't be rendered when no player see them")
                .define("block cluster render optimization", false);
        KILL_WITHER_STORM_MOD_ENTITIES_AFTER_COMMAND_BLOCK_DIES = CONFIG_BUILDER
                .comment("Entities that will be killed: Block Cluster, Sickened Skeleton, Sickened Creeper, Sickened Spider, Sickened Zombie, Tentacle, Withered Symbiont.",
                        "Inspired by Lag Removal mod.")
                .define("kill witherstormmod entities after command block dies", false);
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER
                .push("L_Ender's Cataclysm Extra Settings");
        IGNITIUM_ARMOR_HAS_DAMAGE = CONFIG_BUILDER
                .define("Ignitium Armor has damage", false);
        FINAL_FRACTAL_HAS_DAMAGE = CONFIG_BUILDER
                .define("Final Fractal has damage", false);
        INFERNAL_FORGE_HAS_DAMAGE = CONFIG_BUILDER
                .define("Infernal Forge has damage", false);
        MONSTROUS_HELM_HAS_DAMAGE = CONFIG_BUILDER
                .define("Monstrous Helm has damage", false);
        ZWEIENDER_HAS_DAMAGE = CONFIG_BUILDER
                .define("Zweiender has damage", false);
        VOID_CORE_COOL_DOWN = CONFIG_BUILDER
                .defineInRange("void core cool down, in ticks", 120, 1, Integer.MAX_VALUE);
        INFERNAL_FORGE_COOL_DOWN = CONFIG_BUILDER
                .defineInRange("infernal forge cool down, in ticks", 80, 1, Integer.MAX_VALUE);
        INCINERATOR_COOL_DOWN = CONFIG_BUILDER
                .defineInRange("incinerator cool down, in ticks", 400, 1, Integer.MAX_VALUE);
        SOUL_BLACK_SMITH_SPACING = CONFIG_BUILDER
                .defineInRange("soul black smith spacing", 90, 5, Integer.MAX_VALUE);
        SOUL_BLACK_SMITH_SEPARATION = CONFIG_BUILDER
                .defineInRange("soul black smith separation", 70, 5, Integer.MAX_VALUE);
        RUINED_CITADEL_SPACING = CONFIG_BUILDER
                .defineInRange("ruined citadel spacing", 30, 5, Integer.MAX_VALUE);
        RUINED_CITADEL_SEPARATION = CONFIG_BUILDER
                .defineInRange("ruined citadel separation", 10, 5, Integer.MAX_VALUE);
        BURNING_ARENA_SPACING = CONFIG_BUILDER
                .defineInRange("burning arena spacing", 112, 5, Integer.MAX_VALUE);
        BURNING_ARENA_SEPARATION = CONFIG_BUILDER
                .defineInRange("burning arena separation", 70, 5, Integer.MAX_VALUE);
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER
                .push("The Undergarden Extra Settings");
        CATACOMB_SPACING = CONFIG_BUILDER
                .defineInRange("catacomb spacing", 24, 5, Integer.MAX_VALUE);
        CATACOMB_SEPARATION = CONFIG_BUILDER
                .defineInRange("catacomb separation", 8, 5, Integer.MAX_VALUE);
        CONFIG_BUILDER.pop();

        COMMON_CONFIG = CONFIG_BUILDER.build();
    }
}

package com.teampotato.opotato.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class PotatoCommonConfig {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.BooleanValue ALLOW_LIMIT_MAX_SPAWN, ENABLE_BLUE_SKIES_NERF, ALLOW_EVERY_MOD_GEN_FEATURE_IN_DIM, ENABLE_HEADSHOT_SOUND_DING, KILL_WITHER_STORM_MOD_ENTITIES_AFTER_COMMAND_BLOCK_DIES, LET_WITHER_SICKNESS_ONLY_TAKE_EFFECT_ON_PLAYERS, LET_MOBS_IMMUNE_TO_WITHER_SICKNESS_TICKING_SYSTEM, LET_ANIMALS_IMMUNE_TO_WITHER_SICKNESS_TICKING_SYSTEM, BLOCK_CLUSTER_RENDER_OPTIMIZATION, ENABLE_CREATIVE_ONE_POUCH, DISABLE_ARS_NOUVEAU_MANA_GEM_IN_BASIC_LOOT, IGNITIUM_ARMOR_HAS_DAMAGE, FINAL_FRACTAL_HAS_DAMAGE, INFERNAL_FORGE_HAS_DAMAGE, MONSTROUS_HELM_HAS_DAMAGE, ZWEIENDER_HAS_DAMAGE;
    public static ForgeConfigSpec.IntValue GATE_KEEPER_HOUSE_SPACING, MAX_ENTITIES_NUMBER_PER_CHUNK, SOUL_BLACK_SMITH_SPACING, SOUL_BLACK_SMITH_SEPARATION, RUINED_CITADEL_SPACING, RUINED_CITADEL_SEPARATION, BURNING_ARENA_SPACING, BURNING_ARENA_SEPARATION, CATACOMB_SPACING, CATACOMB_SEPARATION, INCINERATOR_COOL_DOWN, INFERNAL_FORGE_COOL_DOWN, VOID_CORE_COOL_DOWN, APPRENTICE_ARMOR_MAX_MANA_BOOST, APPRENTICE_ARMOR_MANA_REGEN_BONUS, MASTER_ARMOR_MAX_MANA_BOOST, NOVICE_ARMOR_MAX_MANA_BOOST, MASTER_ARMOR_MANA_REGEN_BONUS, NOVICE_ARMOR_MANA_REGEN_BONUS, MANA_BOOST_ENCHANTMENT_MAX_LEVEL, MORIRS_DEATH_WISH_DURABILITY_AMOUNT, DURABILITY_MORIRS_DEATH_WISH_GIVE_ON_DEATH, MORIRS_DEATH_WISH_COOL_DOWN_TICKS,MORIRS_LIFE_BOUND_DURABILITY_GIVE_ON_HEAL, MORIRS_LIFE_BOUND_DURABILITY_LOST_ON_DEATH, GUIDENS_REGRET_DURABILITY_GIVE_ON_KILL, ENVIOUS_KIND_EFFECT_DURATION, EYESORE_ARROW_EXPLODE_TICKS, EYESORE_ARROW_EXPLOSION_DAMAGE, EYESORE_ARROW_BLINDNESS_DURATION, EYESORE_ARROW_SLOWNESS_DURATION, THORN_IN_FLESH_EFFECT_INTERVAL;
    public static ForgeConfigSpec.ConfigValue<Float> HEADSHOT_VOLUME, HEADSHOT_PITCH, LAST_SWEET_DREAM_DISAPPEAR_PERCENT, THORN_IN_FLESH_DAMAGE_ON_PLAYER;
    public static ForgeConfigSpec.DoubleValue MORIRS_DEATH_WISH_MAX_DAMAGE_IN_COUNT, MORIRS_LIFE_BOUND_MAX_DAMAGE_IN_COUNT, ENVIOUS_KIND_DIFF_HEALTH;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BLACKLIST;

    static {
        ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();

        CONFIG_BUILDER.comment("Opotato Common Config").push("Opotato Settings");
        ALLOW_LIMIT_MAX_SPAWN = CONFIG_BUILDER
                .define("allow entities spawn limit", false);
        MAX_ENTITIES_NUMBER_PER_CHUNK = CONFIG_BUILDER
                .defineInRange("max entities spawning number per chunk", 10, 1, 100);
        BLACKLIST = CONFIG_BUILDER
                .defineList("entities whose spawn won't be limited", Lists.newArrayList("modid:mobid"), o -> o instanceof String);
        ENABLE_CREATIVE_ONE_POUCH = CONFIG_BUILDER
                .define("enable creative one pouch", true);
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER.push("MarbleGate's Exotic Enchantment: Flowing Agony Extra Settings");
        CONFIG_BUILDER.push("Morir's Deathwish");
        MORIRS_DEATH_WISH_DURABILITY_AMOUNT = CONFIG_BUILDER
                .defineInRange("The max durability Morir's Deathwish will give on hurt", 3, 1, Integer.MAX_VALUE);
        MORIRS_DEATH_WISH_MAX_DAMAGE_IN_COUNT = CONFIG_BUILDER
                .defineInRange("The max damage that will be counted in Morir's Deathwish", 100.0, 1.0, Double.MAX_VALUE);
        DURABILITY_MORIRS_DEATH_WISH_GIVE_ON_DEATH = CONFIG_BUILDER
                .defineInRange("The durability Morir's Deathwish will give on death", 64, 1, Integer.MAX_VALUE);
        MORIRS_DEATH_WISH_COOL_DOWN_TICKS = CONFIG_BUILDER
                .defineInRange("Cooldown ticks of Morir's Deathwish", 12000, 1, Integer.MAX_VALUE);
        CONFIG_BUILDER.pop();
        CONFIG_BUILDER.push("Morir's Lifebound");
        MORIRS_LIFE_BOUND_DURABILITY_GIVE_ON_HEAL = CONFIG_BUILDER
                .defineInRange("The max durability Morir's Lifebound will give on heal", 10, 1, Integer.MAX_VALUE);
        MORIRS_LIFE_BOUND_MAX_DAMAGE_IN_COUNT = CONFIG_BUILDER
                .defineInRange("The max heal amount that will be counted in Morirs' Life bound", 100.0, 1.0, Double.MAX_VALUE);
        MORIRS_LIFE_BOUND_DURABILITY_LOST_ON_DEATH = CONFIG_BUILDER
                .defineInRange("The durability Morir's Lifebound will take on death", 32, 1, Integer.MAX_VALUE);
        CONFIG_BUILDER.pop();
        CONFIG_BUILDER.push("Guiden's Regret");
        GUIDENS_REGRET_DURABILITY_GIVE_ON_KILL = CONFIG_BUILDER
                .defineInRange("The max durability Guiden's Regret will give on killing an entity", 3, 1, Integer.MAX_VALUE);
        CONFIG_BUILDER.pop();
        CONFIG_BUILDER.push("Last Sweet Dream");
        LAST_SWEET_DREAM_DISAPPEAR_PERCENT = CONFIG_BUILDER
                .defineInRange("How many percents of durability will the item with this enchantment disappear on toss", 0.1F, 0.1F, 0.9F, Float.class);
        CONFIG_BUILDER.pop();
        CONFIG_BUILDER.push("Envious Kind");
        ENVIOUS_KIND_DIFF_HEALTH = CONFIG_BUILDER
                .defineInRange("How much health will be one buff level", 10.0, 1.0, Double.MAX_VALUE);
        ENVIOUS_KIND_EFFECT_DURATION = CONFIG_BUILDER
                .defineInRange("How long will the Envious Being effect be given (ticks)", 200, 1, Integer.MAX_VALUE);
        CONFIG_BUILDER.pop();
        CONFIG_BUILDER.push("Eyesore");
        EYESORE_ARROW_EXPLODE_TICKS = CONFIG_BUILDER
                .defineInRange("How long will the Eyesore Arrow explode since being shot out", 61, 1, Integer.MAX_VALUE);
        EYESORE_ARROW_EXPLOSION_DAMAGE = CONFIG_BUILDER
                .comment("The explosion calculation: basic damage + (enchantment level - 1) * 2")
                .defineInRange("How many basic damage will the Eyesore Arrow explosion cause", 3, 1, Integer.MAX_VALUE);
        EYESORE_ARROW_BLINDNESS_DURATION = CONFIG_BUILDER
                .comment("Blindness length calculation: basic length + 20 * (enchantment level - 1)")
                .defineInRange("How long will the blindness caused by the Eyesore Arrow last basically (in ticks)", 60, 1, Integer.MAX_VALUE);
        EYESORE_ARROW_SLOWNESS_DURATION = CONFIG_BUILDER
                .comment("Slowness length calculation: basic length + 20 * (enchantment level - 1)")
                .defineInRange("How long will the slowness caused by the Eyesore Arrow last basically (in ticks)", 60, 1, Integer.MAX_VALUE);
        CONFIG_BUILDER.pop();
        CONFIG_BUILDER.push("Thorn In Flesh");
        THORN_IN_FLESH_DAMAGE_ON_PLAYER = CONFIG_BUILDER
                .defineInRange("How many Thorn damages Thorn In Flesh will give on player", 1.0F, 0.0F, Float.MAX_VALUE, Float.class);
        THORN_IN_FLESH_EFFECT_INTERVAL = CONFIG_BUILDER
                .defineInRange("Effective interval (stop moving for entities, take 1 thorn damage for players)", 50, 0, Integer.MAX_VALUE);
        CONFIG_BUILDER.pop();
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER.push("Blue Skies Extra Settings");
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
        APPRENTICE_ARMOR_MAX_MANA_BOOST = CONFIG_BUILDER
                .defineInRange("Apprentice Armor Max Mana Boost", 40, 1, Integer.MAX_VALUE);
        APPRENTICE_ARMOR_MANA_REGEN_BONUS = CONFIG_BUILDER
                .defineInRange("Apprentice Armor Mana Regen Bonus", 4, 1, Integer.MAX_VALUE);
        MASTER_ARMOR_MAX_MANA_BOOST = CONFIG_BUILDER
                .defineInRange("Master Armor Max Mana Boost", 80, 1, Integer.MAX_VALUE);
        MASTER_ARMOR_MANA_REGEN_BONUS = CONFIG_BUILDER
                .defineInRange("Master Armor Max Mana Boost", 6, 1, Integer.MAX_VALUE);
        NOVICE_ARMOR_MAX_MANA_BOOST = CONFIG_BUILDER
                .defineInRange("Novice Armor Max Mana Boost", 25, 1, Integer.MAX_VALUE);
        NOVICE_ARMOR_MANA_REGEN_BONUS = CONFIG_BUILDER
                .defineInRange("Novice Armor Max Mana Boost", 2, 1, Integer.MAX_VALUE);
        MANA_BOOST_ENCHANTMENT_MAX_LEVEL = CONFIG_BUILDER
                .defineInRange("Mana Boost Enchantment Max Level", 3, 1, Integer.MAX_VALUE);
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER.push("Headshot Extra Settings");
        ENABLE_HEADSHOT_SOUND_DING = CONFIG_BUILDER
                .define("enable 'Ding' on Headshot", true);
        HEADSHOT_VOLUME = CONFIG_BUILDER
                .comment("Range: 0.0F ~ 1.0F. Type: Float")
                .defineInRange("'Ding' volume", 1.0F, 0.0F, Float.MAX_VALUE, Float.class);
        HEADSHOT_PITCH = CONFIG_BUILDER
                .comment("Range: 0.5F ~ 2.0F. Type: Float")
                .defineInRange("'Ding' pitch", 1.0F, 0.0F, Float.MAX_VALUE, Float.class);
        CONFIG_BUILDER.pop();

        CONFIG_BUILDER.push("Cracker's Wither Storm Mod Optimization Settings").comment("Every settings here will improve performance a lot as the mod is really laggy");
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

        CONFIG_BUILDER.push("L_Ender's Cataclysm Extra Settings");
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

        CONFIG_BUILDER.push("The Undergarden Extra Settings");
        CATACOMB_SPACING = CONFIG_BUILDER
                .defineInRange("catacomb spacing", 24, 5, Integer.MAX_VALUE);
        CATACOMB_SEPARATION = CONFIG_BUILDER
                .defineInRange("catacomb separation", 8, 5, Integer.MAX_VALUE);
        CONFIG_BUILDER.pop();

        COMMON_CONFIG = CONFIG_BUILDER.build();
    }
}

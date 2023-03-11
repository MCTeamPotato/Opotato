package com.teampotato.opotato.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class PotatoCommonConfig {
    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec.BooleanValue ALTERNATE_CURRENT_DEBUG_MODE, ENABLE_FENCE_JUMP, DO_BLIND, DO_NAUSEA, ENABLE_HEADSHOT, PLAY_HEADSHOT_SOUND;
    public static final ForgeConfigSpec.DoubleValue DAMAGE_MULTIPLIER;
    public static final ForgeConfigSpec.IntValue BLIND_TICKS, NAUSEA_TICKS;

    static {
        ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();
        CONFIG_BUILDER.push("Opotato Common Config");
        CONFIG_BUILDER.comment("[Alternate Current]");
        ALTERNATE_CURRENT_DEBUG_MODE = CONFIG_BUILDER.define("enable alternate current debug mode", false);

        ENABLE_FENCE_JUMP = CONFIG_BUILDER
                .comment(
                        "-----------------------------------",
                        "[Jump Over Fences]",
                        "Compared with the 'Jump Over Fences' mod, the fence jumping function of this mod is better optimized.",
                        "I(Team Potato Coder, Kasualix) had experienced crash with 'Jump Over Fences' before, so I don't want to use it again and wrote this instead.",
                        "This is disabled by default, because this is not optimization & don't fit well with the concept of Opotato, but I do need this even for my personal use.",
                        "You can enable this if you like. (yea of course)"
                )
                .define("enable fence jump", false);
        CONFIG_BUILDER.comment(
                "-----------------------------------",
                "[Headshot]",
                "This is NOT a copy of chronosacaria's Headshot-Forge",
                "Yeah I sent a PR to improve it, but he seemed to abandon 1.16.5 Forge, so I don't want to wait for him anymore.",
                "This version is more like the original SilverAndro's Fabric Headshot, with utils to check whether an arrow hit is on entity's head.",
                "What's more, you can also receive feedback sound (ARROW_HIT_PLAYER, also known as DING) when headshot events trigger.",
                "You can also use resourcepack to customize the status message when headshot events trigger (Yeah in the lang file)."
        );
        ENABLE_HEADSHOT = CONFIG_BUILDER
                .define("enable headshot", false);
        DO_BLIND = CONFIG_BUILDER
                .define("do blind", true);
        BLIND_TICKS = CONFIG_BUILDER
                .defineInRange("blind ticks (you should enable 'do blind' to make this work)", 60, 1, Integer.MAX_VALUE);
        DO_NAUSEA = CONFIG_BUILDER
                .define("do nausea", true);
        NAUSEA_TICKS = CONFIG_BUILDER
                .defineInRange("nausea ticks (you should enable 'do nausea' to make this work)", 60, 1, Integer.MAX_VALUE);
        DAMAGE_MULTIPLIER = CONFIG_BUILDER
                .defineInRange("damage multiplier on headshots, use 1.00 for no damage multiply", 2.56, 1.00, Double.MAX_VALUE);
        PLAY_HEADSHOT_SOUND = CONFIG_BUILDER
                .define("player headshot feedback sound", true);
        CONFIG_BUILDER.pop();
        COMMON_CONFIG = CONFIG_BUILDER.build();
    }
}

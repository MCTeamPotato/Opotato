package com.teampotato.opotato.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class PotatoCommonConfig {
    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec.BooleanValue ALTERNATE_CURRENT_DEBUG_MODE, ENABLE_FENCE_JUMP, DO_BLIND, DO_NAUSEA, ENABLE_HEADSHOT, PLAY_HEADSHOT_SOUND, ENABLE_CHATGPT;
    public static final ForgeConfigSpec.DoubleValue DAMAGE_MULTIPLIER;
    public static final ForgeConfigSpec.IntValue BLIND_TICKS, NAUSEA_TICKS;
    static {
        ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();
        CONFIG_BUILDER.push("Opotato Common Config");
        CONFIG_BUILDER.push("ChatGPT In MC");
        ENABLE_CHATGPT = CONFIG_BUILDER
                .define("Enable ChatGPT", false);
        CONFIG_BUILDER.push("Alternate Current");
        ALTERNATE_CURRENT_DEBUG_MODE = CONFIG_BUILDER
                .comment("This is for dev.")
                .define("Enable alternate current debug mode", false);
        CONFIG_BUILDER.pop();
        CONFIG_BUILDER.push("Jump Over Fences");
        ENABLE_FENCE_JUMP = CONFIG_BUILDER
                .comment(
                        "Compared with the 'Jump Over Fences' mod, the fence jumping function of this mod is better optimized.",
                        "I(Team Potato Coder, Kasualix) had experienced crash with 'Jump Over Fences' before, so I don't want to use it again and wrote this instead.",
                        "This is disabled by default, because this is not optimization & don't fit well with the concept of Opotato, but I do need this even for my personal use.",
                        "You can enable this if you like. (yea of course)"
                )
                .define("Enable fence jump", false);
        CONFIG_BUILDER.pop();
        CONFIG_BUILDER.push("Headshot");
        ENABLE_HEADSHOT = CONFIG_BUILDER.comment(
                "This is not a copy of chronosacaria's Headshot-Forge",
                "I sent a PR to improve it, but he seemed to abandon 1.16.5 Forge, so I don't want to wait for him anymore.",
                "This version is more like the original SilverAndro's Fabric Headshot, with utils to check whether an arrow hit is on entity's head.",
                "What's more, you can also receive feedback sound (ARROW_HIT_PLAYER, also known as DING) when headshot events trigger.",
                "You can also use resourcepack to customize the status message when headshot events trigger (Yeah in the lang file).",
                "disable by default, enable this if you like."
                )
                .define("Enable headshot", false);
        PLAY_HEADSHOT_SOUND = CONFIG_BUILDER
                .define("Player headshot feedback sound", true);
        DAMAGE_MULTIPLIER = CONFIG_BUILDER
                .defineInRange("Damage multiplier on headshots", 2.56, 1.00, 1000.00);
        DO_BLIND = CONFIG_BUILDER
                .define("Do blind", true);
        BLIND_TICKS = CONFIG_BUILDER
                .defineInRange("Blind ticks (you should enable 'do blind' to make this work)", 60, 1, Integer.MAX_VALUE);
        DO_NAUSEA = CONFIG_BUILDER
                .define("Do nausea", true);
        NAUSEA_TICKS = CONFIG_BUILDER
                .defineInRange("Nausea ticks (you should enable 'do nausea' to make this work)", 60, 1, Integer.MAX_VALUE);
        CONFIG_BUILDER.pop();
        COMMON_CONFIG = CONFIG_BUILDER.build();
    }
}

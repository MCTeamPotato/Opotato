package com.teampotato.opotato.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class PotatoCommonConfig {
    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec.BooleanValue ALTERNATE_CURRENT_DEBUG_MODE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_FENCE_JUMP;

    static {
        ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();
        CONFIG_BUILDER.comment("Opotato").push("alternatecurrent");
        ALTERNATE_CURRENT_DEBUG_MODE = CONFIG_BUILDER.define("enable alternate current debug mode", false);
        ENABLE_FENCE_JUMP = CONFIG_BUILDER
                .comment(
                        "Compared with the 'Jump Over Fences' mod, the fence jumping function of this mod is better optimized.",
                        "I had experienced crash with 'Jump Over Fences' before, so I don't want to use it again and wrote this instead.",
                        "This is disabled by default, because this is not optimization & don't fit well with the concept of Opotato."
                )
                .define("enable fence jump", false);
        CONFIG_BUILDER.pop();
        COMMON_CONFIG = CONFIG_BUILDER.build();
    }
}

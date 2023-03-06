package com.teampotato.opotato.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class PotatoCommonConfig {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.BooleanValue ALTERNATE_CURRENT_DEBUG_MODE;

    static {
        ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();
        CONFIG_BUILDER.comment("Opotato").push("alternatecurrent");
        ALTERNATE_CURRENT_DEBUG_MODE = CONFIG_BUILDER.define("enable alternate current debug mode", false);
        CONFIG_BUILDER.pop();
        COMMON_CONFIG = CONFIG_BUILDER.build();
    }
}

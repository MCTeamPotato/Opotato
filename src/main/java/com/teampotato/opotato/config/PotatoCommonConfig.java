package com.teampotato.opotato.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class PotatoCommonConfig {
    public static final ForgeConfigSpec potatoConfig;
    public static final ForgeConfigSpec.BooleanValue enableCreativeOnePouch;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("OpotatoCommonConfig");
        enableCreativeOnePouch = builder.define("enableCreativeOnePouch", false);
        builder.pop();
        potatoConfig = builder.build();
    }
}

package com.teampotato.opotato.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class WitherStormExtraConfig {
    public static final ForgeConfigSpec witherStormConfig;
    public static final ForgeConfigSpec.BooleanValue shouldRandomlyReduceBlockClusterRendering;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("WitherStormExtraConfig");
        shouldRandomlyReduceBlockClusterRendering = builder.define("shouldRandomlyReduceBlockClusterRendering", true);
        builder.pop();
        witherStormConfig = builder.build();
    }
}

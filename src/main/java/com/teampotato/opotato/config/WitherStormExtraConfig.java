package com.teampotato.opotato.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class WitherStormExtraConfig {
    public static final ForgeConfigSpec witherStormConfig;
    public static final ForgeConfigSpec.BooleanValue shouldRandomlyReduceBlockClusterRendering;
    public static final ForgeConfigSpec.BooleanValue killAllWitherStormModEntitiesWhenTheCommandBlockDies;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("WitherStormExtraConfig");
        killAllWitherStormModEntitiesWhenTheCommandBlockDies = builder.define("killAllWitherStormModEntitiesWhenTheCommandBlockDies", true);
        shouldRandomlyReduceBlockClusterRendering = builder.define("shouldRandomlyReduceBlockClusterRendering", true);
        builder.pop();
        witherStormConfig = builder.build();
    }
}

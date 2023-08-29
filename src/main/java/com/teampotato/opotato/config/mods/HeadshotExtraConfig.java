package com.teampotato.opotato.config.mods;

import net.minecraftforge.common.ForgeConfigSpec;

public class HeadshotExtraConfig {
    public static final ForgeConfigSpec headshotConfig;
    public static final ForgeConfigSpec.BooleanValue enableDingOnHeadshot;
    public static final ForgeConfigSpec.DoubleValue dingOnHeadshotVolume, dingOnHeadshotPitch;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("HeadshotExtraConfig");
        enableDingOnHeadshot = builder.define("enableDingOnHeadshot", true);
        dingOnHeadshotVolume = builder.defineInRange("dingOnHeadshotVolume", 1.00, 0.01, Double.MAX_VALUE);
        dingOnHeadshotPitch = builder.defineInRange("dingOnHeadshotPitch", 1.00, 0.01, Double.MAX_VALUE);
        builder.pop();
        headshotConfig = builder.build();
    }
}

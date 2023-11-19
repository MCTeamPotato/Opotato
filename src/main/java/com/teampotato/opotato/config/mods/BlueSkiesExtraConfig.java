package com.teampotato.opotato.config.mods;

import net.minecraftforge.common.ForgeConfigSpec;

public class BlueSkiesExtraConfig {
    public static final ForgeConfigSpec blueSkiesExtraConfig;
    public static final ForgeConfigSpec.BooleanValue allowEveryModFeatureGenInTheDims;
    public static final ForgeConfigSpec.BooleanValue enableDimensionalNerf;
    public static final ForgeConfigSpec.BooleanValue enableEnhancedDimensionalNerf;
    public static final ForgeConfigSpec.IntValue gateKeeperHouseSpacing;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("BlueSkiesExtraConfig");
        enableDimensionalNerf = builder.define("enableDimensionalNerf", true);
        enableEnhancedDimensionalNerf = builder.comment("If enabled, armors and weapons from other mods should be totally useless in blue skies dimensions").define("enableEnhancedDimensionalNerf", false);
        allowEveryModFeatureGenInTheDims = builder.define("allowEveryModFeatureGenInTheDims", false);
        gateKeeperHouseSpacing = builder.defineInRange("gateKeeperHouseSpacing", 18, 1, Integer.MAX_VALUE);
        builder.pop();
        blueSkiesExtraConfig = builder.build();
    }
}

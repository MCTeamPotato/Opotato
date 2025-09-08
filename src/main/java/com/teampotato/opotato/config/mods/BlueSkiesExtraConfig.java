package com.teampotato.opotato.config.mods;

import net.minecraftforge.common.ForgeConfigSpec;

public class BlueSkiesExtraConfig {
    public static final ForgeConfigSpec blueSkiesExtraConfig;
    public static final ForgeConfigSpec.BooleanValue allowEveryModFeatureGenInTheDims;
    public static final ForgeConfigSpec.BooleanValue enableDimensionalNerf;
    public static final ForgeConfigSpec.BooleanValue lockGamma;
    public static final ForgeConfigSpec.BooleanValue enableEnhancedDimensionalNerf;
    public static final ForgeConfigSpec.IntValue gateKeeperHouseSpacing;
    public static final ForgeConfigSpec.ConfigValue<? extends String> itemToTradeZealLighter;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("BlueSkiesExtraConfig");
        lockGamma = builder.define("LockGamma", false);
        enableDimensionalNerf = builder.define("enableDimensionalNerf", false);
        enableEnhancedDimensionalNerf = builder.comment("If enabled, armors and weapons from other mods should be totally useless in blue skies dimensions").define("enableEnhancedDimensionalNerf", false);
        allowEveryModFeatureGenInTheDims = builder.define("allowEveryModFeatureGenInTheDims", false);
        gateKeeperHouseSpacing = builder.defineInRange("gateKeeperHouseSpacing", 18, 1, Integer.MAX_VALUE);
        itemToTradeZealLighter = builder.define("itemToTradeZealLighter", "minecraft:emerald");
        builder.pop();
        blueSkiesExtraConfig = builder.build();
    }
}

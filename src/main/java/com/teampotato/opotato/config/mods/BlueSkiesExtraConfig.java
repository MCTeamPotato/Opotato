package com.teampotato.opotato.config.mods;

import net.minecraftforge.common.ForgeConfigSpec;

public class BlueSkiesExtraConfig {
    public static final ForgeConfigSpec blueSkiesExtraConfig;
    public static final ForgeConfigSpec.BooleanValue allowEveryModFeatureGenInTheDims;
    public static final ForgeConfigSpec.BooleanValue lockGamma;
    public static final ForgeConfigSpec.IntValue gateKeeperHouseSpacing;
    public static final ForgeConfigSpec.ConfigValue<? extends String> itemToTradeZealLighter;

    public static final ForgeConfigSpec.BooleanValue enablePlayerArmorValueNerf;
    public static final ForgeConfigSpec.BooleanValue enableDamageNerf;
    public static final ForgeConfigSpec.BooleanValue enableBreakNerf;
    public static final ForgeConfigSpec.BooleanValue requireProgressionForToolUsage;
    public static final ForgeConfigSpec.BooleanValue onlySkyAxeHurtEntWall;
    public static final ForgeConfigSpec.BooleanValue arachnarchShieldNerf;

    public static final ForgeConfigSpec.BooleanValue crash;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("BlueSkiesExtraConfig");
        crash = builder.comment("I'm just wondering who is consuming the Blue Skies shit :)").define("crashYourGameIfBlueSkiesIsDetected", false);
        lockGamma = builder.define("LockGamma", false);
        allowEveryModFeatureGenInTheDims = builder.define("allowEveryModFeatureGenInTheDims", false);
        gateKeeperHouseSpacing = builder.defineInRange("gateKeeperHouseSpacing", 18, 1, Integer.MAX_VALUE);
        itemToTradeZealLighter = builder.define("itemToTradeZealLighter", "minecraft:emerald");
        builder.push("DimensionNerf");
        enablePlayerArmorValueNerf = builder.define("enablePlayerArmorValueNerfInDim", false);
        enableDamageNerf = builder.define("enablePlayerDamageNerfInDim", false);
        enableBreakNerf = builder.define("enablePlayerBreakBlockNerfInDim", false);
        requireProgressionForToolUsage = builder.define("requireProgressionForToolUsageInDim", false);
        onlySkyAxeHurtEntWall = builder.define("onlySkyAxeHurtEntWall", false);
        arachnarchShieldNerf = builder.define("enableArachnarchShieldNerf", false);
        builder.pop();

        builder.pop();
        blueSkiesExtraConfig = builder.build();
    }
}

package com.teampotato.potatoptimize;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {

    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec.IntValue TARGET_FPS_IN_GAME_GUI;
    public static final ForgeConfigSpec.IntValue TARGET_FPS_IN_GAME_GUI_ANIMATED;
    public static final ForgeConfigSpec.IntValue TARGET_FPS_IN_GAME_SCREENS;
    public static final ForgeConfigSpec.IntValue TARGET_FPS_NAME_TAGS;
    public static final ForgeConfigSpec.BooleanValue ENABLED_GUI;
    public static final ForgeConfigSpec.BooleanValue ENABLED_GUI_ANIMATION_SPEEDUP;
    public static final ForgeConfigSpec.BooleanValue ENABLED_SCREENS;
    public static final ForgeConfigSpec.BooleanValue ENABLE_SIGN_BUFFERING;
    public static final ForgeConfigSpec.BooleanValue ENABLE_NAME_TAG_SCREEN_BUFFERING;

    static {
        ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();
        CONFIG_BUILDER.comment("PotatOptimize").push("exordium");
        TARGET_FPS_IN_GAME_GUI = CONFIG_BUILDER.defineInRange("targetFPSIngameGui", 20, 10, Integer.MAX_VALUE);
        TARGET_FPS_IN_GAME_GUI_ANIMATED = CONFIG_BUILDER.defineInRange("targetFPSIngameGuiAnimated", 60, 10, Integer.MAX_VALUE);
        TARGET_FPS_IN_GAME_SCREENS = CONFIG_BUILDER.defineInRange("targetFPSIngameScreens", 60, 10, Integer.MAX_VALUE);
        TARGET_FPS_NAME_TAGS = CONFIG_BUILDER.defineInRange("targetFPSNameTags", 60, 10, Integer.MAX_VALUE);
        ENABLED_GUI = CONFIG_BUILDER.define("enabledGui", true);
        ENABLED_GUI_ANIMATION_SPEEDUP = CONFIG_BUILDER.define("enabledGuiAnimationSpeedup", true);
        ENABLED_SCREENS = CONFIG_BUILDER.define("enabledScreens", true);
        ENABLE_SIGN_BUFFERING = CONFIG_BUILDER.define("enableSignBuffering", true);
        ENABLE_NAME_TAG_SCREEN_BUFFERING = CONFIG_BUILDER.define("enableNametagScreenBuffering", false);
        CONFIG_BUILDER.pop();
        COMMON_CONFIG = CONFIG_BUILDER.build();
    }
}

package com.teampotato.opotato.config.mods;

import net.minecraftforge.common.ForgeConfigSpec;

public class FlowingAgonyExtraConfig {
    public static final ForgeConfigSpec flowingAgonyConfig;
    public static final ForgeConfigSpec.DoubleValue commonUndeadDamageReductionPercent, rareUndeadDamageReductionPercent, witherDamageReductionPercent;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("FlowingAgonyExtraConfig");
        builder.push("TheMistakens");
        builder.push("CorruptedKindred");
        commonUndeadDamageReductionPercent = builder.defineInRange("commonUndeadDamageReductionPercent", 0.50, 0.00, Double.MAX_VALUE);
        rareUndeadDamageReductionPercent = builder.defineInRange("rareUndeadDamageReductionPercent", 0.10, 0.00, Double.MAX_VALUE);
        witherDamageReductionPercent = builder.defineInRange("witherDamageReductionPercent", 0.05F, 0.00, Double.MAX_VALUE);
        builder.pop();
        builder.pop();
        builder.pop();
        flowingAgonyConfig = builder.build();
    }
}

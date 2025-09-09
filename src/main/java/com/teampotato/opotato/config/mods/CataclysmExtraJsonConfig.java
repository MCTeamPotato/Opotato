package com.teampotato.opotato.config.mods;

import com.teampotato.opotato.config.JsonConfig;
import net.minecraftforge.fml.loading.FMLLoader;

public class CataclysmExtraJsonConfig {
    private static final JsonConfig INSTANCE = JsonConfig
            .create(FMLLoader.getGamePath().resolve("config").resolve("opotato").resolve("mods").resolve("cataclysm-extra.json"), "1.0.0")
            .put("BulwarkOfTheFlameDamageable", false)
            .put("BulwarkOfTheFlameDurability", 350)
            .put("IncineratorDamageable", false)
            .put("IncineratorDurability", 560)
            .put("VoidCoreDamageable", false)
            .put("VoidCoreDurability", 150)
            .put("GauntletOfGuardDamageable", false)
            .put("GauntletOfGuardDurability", 210)
            .initialize();

    public static final boolean bulwarkOfTheFlameDamageable = INSTANCE.getBoolean("BulwarkOfTheFlameDamageable");
    public static final int bulwarkOfTheFlameDurability = INSTANCE.getInt("BulwarkOfTheFlameDurability");

    public static final boolean incineratorDamageable = INSTANCE.getBoolean("IncineratorDamageable");
    public static final int incineratorDurability = INSTANCE.getInt("IncineratorDurability");

    public static final boolean voidCoreDamageable = INSTANCE.getBoolean("VoidCoreDamageable");
    public static final int voidCoreDurability = INSTANCE.getInt("VoidCoreDurability");

    public static final boolean gauntletOfGuardDamageable = INSTANCE.getBoolean("GauntletOfGuardDamageable");
    public static final int gauntletOfGuardDurability = INSTANCE.getInt("GauntletOfGuardDurability");

    public static void init() {}
}

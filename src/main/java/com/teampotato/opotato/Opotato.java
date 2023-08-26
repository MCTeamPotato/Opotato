package com.teampotato.opotato;

import com.teampotato.opotato.config.ArsNouveauLootConfig;
import com.teampotato.opotato.config.BlueSkiesExtraConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(Opotato.MOD_ID)
public class Opotato {
    public static final String MOD_ID = "opotato";

    public Opotato() {
        ModConfig.Type common = ModConfig.Type.COMMON;
        ModLoadingContext ctx = ModLoadingContext.get();
        ctx.registerConfig(common, ArsNouveauLootConfig.arsNouveauConfig, MOD_ID + "/arsNouveau-loot.toml");
        ctx.registerConfig(common, BlueSkiesExtraConfig.blueSkiesExtraConfig, MOD_ID + "/blueSkies-extra.toml");
    }
}

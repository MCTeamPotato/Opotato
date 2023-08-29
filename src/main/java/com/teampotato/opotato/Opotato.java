package com.teampotato.opotato;

import com.teampotato.opotato.config.*;
import com.teampotato.opotato.events.DEUFix;
import com.teampotato.opotato.events.VoidCoreTriggerEvents;
import com.teampotato.opotato.events.client.KeybindEvents;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Opotato.MOD_ID)
public class Opotato {
    public static final String MOD_ID = "opotato";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static boolean isRubidiumLoaded;

    public static final Direction[] DIRECTIONS = Direction.values();
    public static final EquipmentSlot[] EQUIPMENT_SLOTS = EquipmentSlot.values();

    public Opotato() {
        FMLLoader.getLoadingModList().getMods().stream().sorted()
                .forEach(modInfo -> LOGGER.info("Mod " + modInfo.getOwningFile().getFile().getFileName() + " loaded!"));
        isRubidiumLoaded = isLoaded("rubidium");
        ModConfig.Type common = ModConfig.Type.COMMON;
        ModLoadingContext ctx = ModLoadingContext.get();

        MinecraftForge.EVENT_BUS.register(DEUFix.class);
        if (isLoaded("cataclysm") && isLoaded("curios")) {
            if(FMLLoader.getDist().isClient()) MinecraftForge.EVENT_BUS.register(KeybindEvents.class);
            MinecraftForge.EVENT_BUS.register(VoidCoreTriggerEvents.class);
        }

        if (isLoaded("ars_nouveau")) ctx.registerConfig(common, ArsNouveauLootConfig.arsNouveauConfig, MOD_ID + "/mods/arsNouveau-loot.toml");
        if (isLoaded("blue_skies")) ctx.registerConfig(common, BlueSkiesExtraConfig.blueSkiesExtraConfig, MOD_ID + "/mods/blueSkies-extra.toml");
        if (isLoaded("cataclysm")) ctx.registerConfig(common, CataclysmExtraConfig.cataclysmExtraConfig, MOD_ID + "/mods/cataclysm-extra.toml");
        if (isLoaded("headshot")) ctx.registerConfig(common, HeadshotExtraConfig.headshotConfig, MOD_ID + "/mods/headshot-extra.toml");
        if (isLoaded("undergarden")) ctx.registerConfig(common, UndergardenExtraConfig.undergardenConfig, MOD_ID + "/mods/undergarden-extra.toml");
        ctx.registerConfig(common, PotatoCommonConfig.potatoConfig, MOD_ID + "/opotato-common.toml");
    }

    public static boolean isLoaded(String mod) {
        return FMLLoader.getLoadingModList().getModFileById(mod) != null;
    }
}

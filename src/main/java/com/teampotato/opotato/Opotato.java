package com.teampotato.opotato;

import com.teampotato.opotato.config.PotatoCommonConfig;
import com.teampotato.opotato.config.mods.*;
import com.teampotato.opotato.events.*;
import com.teampotato.opotato.events.client.KeybindEvents;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
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
        isRubidiumLoaded = isLoaded("rubidium");

        initEvents();
        initConfigs(ModLoadingContext.get());
    }

    private static void initEvents() {
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.register(DEUFix.class);

        if (isLoaded("cataclysm") && isLoaded("curios")) {
            if (FMLLoader.getDist().isClient()) bus.register(KeybindEvents.VoidCore.class);
            bus.register(VoidCoreTriggerEvents.class);
        }

        bus.register(KeybindEvents.OnePunch.class);
        bus.register(CreativeOnePunch.class);
        bus.register(DEUFix.class);
        bus.register(PotatoEvents.class);

        if (isLoaded("witherstormmod")) {
            bus.register(WitherStormCaches.class);
        }
    }

    private static void initConfigs(ModLoadingContext ctx) {
        ModConfig.Type common = ModConfig.Type.COMMON;
        if (isLoaded("ars_nouveau")) ctx.registerConfig(common, ArsNouveauLootConfig.arsNouveauConfig, MOD_ID + "/mods/arsNouveau-loot.toml");
        if (isLoaded("blue_skies")) ctx.registerConfig(common, BlueSkiesExtraConfig.blueSkiesExtraConfig, MOD_ID + "/mods/blueSkies-extra.toml");
        if (isLoaded("cataclysm")) ctx.registerConfig(common, CataclysmExtraConfig.cataclysmExtraConfig, MOD_ID + "/mods/cataclysm-extra.toml");
        if (isLoaded("headshot")) ctx.registerConfig(common, HeadshotExtraConfig.headshotConfig, MOD_ID + "/mods/headshot-extra.toml");
        if (isLoaded("undergarden")) ctx.registerConfig(common, UndergardenExtraConfig.undergardenConfig, MOD_ID + "/mods/undergarden-extra.toml");
        ctx.registerConfig(common, PotatoCommonConfig.potatoConfig, MOD_ID + "/opotato-common.toml");
    }

    public static boolean isLoaded(String mod) {
        return ModList.get().isLoaded(mod);
    }
}

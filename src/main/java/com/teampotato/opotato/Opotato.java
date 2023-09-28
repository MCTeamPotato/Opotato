package com.teampotato.opotato;

import com.teampotato.opotato.config.PotatoCommonConfig;
import com.teampotato.opotato.config.json.PotatoJsonConfig;
import com.teampotato.opotato.config.mods.*;
import com.teampotato.opotato.events.*;
import com.teampotato.opotato.events.client.KeybindEvents;
import com.teampotato.opotato.mixin.EarlySetupInitializer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(Opotato.MOD_ID)
public class Opotato {
    public static final String MOD_ID = "opotato";

    public static final Direction[] DIRECTIONS = Direction.values();
    public static final EquipmentSlot[] EQUIPMENT_SLOTS = EquipmentSlot.values();

    public Opotato() {
        if (PotatoJsonConfig.initFailed) throw new RuntimeException("Failed to create json config");

        initEvents();
        initConfigs(ModLoadingContext.get());
    }

    private static void initEvents() {
        IEventBus bus = MinecraftForge.EVENT_BUS;

        if (EarlySetupInitializer.isCataclysmLoaded) {
            bus.register(FlameStrikeDamageEvent.class);
        }

        if (EarlySetupInitializer.isWitherStormModLoaded) bus.register(WitherSicknessUpdate.class);

        bus.register(KeybindEvents.class);
        bus.register(CreativeOnePunch.class);
        bus.register(DuplicateUUIDFix.class);
        bus.register(PotatoEvents.class);
        bus.register(EntitiesCacheEvent.class);
    }

    private static void initConfigs(ModLoadingContext ctx) {
        ModConfig.Type common = ModConfig.Type.COMMON;
        if (EarlySetupInitializer.isLoaded("ars_nouveau")) ctx.registerConfig(common, ArsNouveauLootConfig.arsNouveauConfig, MOD_ID + "/mods/arsNouveau-loot.toml");
        if (EarlySetupInitializer.isLoaded("blue_skies")) ctx.registerConfig(common, BlueSkiesExtraConfig.blueSkiesExtraConfig, MOD_ID + "/mods/blueSkies-extra.toml");
        if (EarlySetupInitializer.isCataclysmLoaded) ctx.registerConfig(common, CataclysmExtraConfig.cataclysmExtraConfig, MOD_ID + "/mods/cataclysm-extra.toml");
        if (EarlySetupInitializer.isLoaded("headshot")) ctx.registerConfig(common, HeadshotExtraConfig.headshotConfig, MOD_ID + "/mods/headshot-extra.toml");
        if (EarlySetupInitializer.isLoaded("undergarden")) ctx.registerConfig(common, UndergardenExtraConfig.undergardenConfig, MOD_ID + "/mods/undergarden-extra.toml");
        if (EarlySetupInitializer.isWitherStormModLoaded) ctx.registerConfig(common, WitherStormExtraConfig.witherStormConfig, MOD_ID + "/mods/witherStormMod-extra.toml");
        ctx.registerConfig(common, PotatoCommonConfig.potatoConfig, MOD_ID + "/opotato-common.toml");
    }
}

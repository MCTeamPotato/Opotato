package com.teampotato.opotato.events;

import com.teampotato.opotato.Opotato;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.versions.forge.ForgeVersion;
import org.jetbrains.annotations.NotNull;

import static com.teampotato.opotato.Opotato.isLoaded;

public class PotatoEvents {
    public static final ObjectArraySet<VillagerProfession> VILLAGER_PROFESSIONS = new ObjectArraySet<>();

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event)  {
        VILLAGER_PROFESSIONS.addAll(ForgeRegistries.PROFESSIONS.getValues());

        if (isLoaded("epicfight") && !ForgeVersion.getVersion().equals("36.2.39") && ModList.get().getModFileById("epicfight").getFile().getFileName().contains("16.6.4")) {
            addIncompatibleWarn(event, "opotato.epicfight.wrong_forge_version");
        }
        if (Opotato.isRubidiumLoaded) {
            if (isLoaded("betterfpsdist")) addIncompatibleWarn(event, "opotato.incompatible.rubidium.betterfpsdist");
            if (isLoaded("immersive_portals")) addIncompatibleWarn(event, "opotato.incompatible.rubidium.immersive_portals");
            if (isLoaded("chunkanimator")) addIncompatibleWarn(event, "opotato.incompatible.rubidium.chunkanimator");
            if (isLoaded("betterbiomeblend")) addIncompatibleWarn(event, "opotato.incompatible.rubidium.betterbiomeblend");
        }
        if (isLoaded("mcdoom") && !isLoaded("mcdoomfix")) addIncompatibleWarn(event, "opotato.mcdoom.without_fix");
        if (isLoaded("magnesium")) {
            if (Opotato.isRubidiumLoaded) {
                addIncompatibleWarn(event, "opotato.incompatible.magnesium.rubidium");
            } else {
                addIncompatibleWarn(event, "opotato.magnesium");
            }
        }
        if (isLoaded("helium")) addIncompatibleWarn(event, "opotato.helium.dangerous");
    }

    private static void addIncompatibleWarn(@NotNull FMLCommonSetupEvent event, String translationKey) {
        event.enqueueWork(() -> ModLoader.get().addWarning(new ModLoadingWarning(ModLoadingContext.get().getActiveContainer().getModInfo(), ModLoadingStage.COMMON_SETUP, translationKey)));
    }
}

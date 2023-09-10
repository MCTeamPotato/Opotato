package com.teampotato.opotato.util.stxck;

import com.teampotato.opotato.config.mods.stxck.StxckForgeClientConfig;
import com.teampotato.opotato.config.mods.stxck.StxckForgeCommonConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;


public class StxckForgeInitializer {
    public static void init() {
        initConfigs();
        replaceAreMergePredicate();
        Staaaaaaaaaaaack.registriesFetcher = ForgeRegistries.ITEMS::getValue;
    }

    private static void initConfigs() {
        Pair<StxckForgeClientConfig, ForgeConfigSpec> clientConfig = new ForgeConfigSpec.Builder().configure(StxckForgeClientConfig::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, clientConfig.getRight());
        Staaaaaaaaaaaack.clientConfig = clientConfig.getLeft();

        Pair<StxckForgeCommonConfig, ForgeConfigSpec> commonConfig = new ForgeConfigSpec.Builder().configure(StxckForgeCommonConfig::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonConfig.getRight());
        Staaaaaaaaaaaack.commonConfig = commonConfig.getLeft();
    }

    private static void replaceAreMergePredicate() {
        StxckUtil.areMergableReplacementPredicate = (a, b) -> {
            if (!a.getItem().equals(b.getItem())) {
                return false;
            } else if (a.hasTag() ^ b.hasTag()) {
                return false;
            } else if (!b.areCapsCompatible(a)) {
                return false;
            } else {
                return !a.hasTag() || (a.getTag() != null && a.getTag().equals(b.getTag()));
            }
        };
    }
}

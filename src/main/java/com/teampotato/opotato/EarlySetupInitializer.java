package com.teampotato.opotato;

import com.teampotato.opotato.config.json.PotatoJsonConfig;
import com.teampotato.opotato.config.mixin.Option;
import com.teampotato.opotato.config.mixin.PotatoMixinConfig;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.ModLoadingWarning;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.util.List;
import java.util.Set;

public class EarlySetupInitializer implements IMixinConfigPlugin {
    public static final String MOD_ID = "opotato";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final String[] WITHER_STORM_CLEANER_TARGETS = new String[]{"block_cluster", "sickened_skeleton", "sickened_creeper", "sickened_spider", "sickened_zombie", "tentacle", "withered_symbiont"};
    private static final String MIXIN_PACKAGE_ROOT = "com.teampotato.opotato.mixin.";
    public static PotatoMixinConfig config;
    public static EarlySetupInitializer instance;
    public static PotatoJsonConfig potatoJsonConfig;

    public static boolean isRubidiumLoaded;
    public static boolean isWitherStormModLoaded;
    public static boolean isCataclysmLoaded;
    public static boolean isNotEnoughRecipeBookLoaded;
    public static boolean isNeatLoaded;

    public EarlySetupInitializer() {
        isNotEnoughRecipeBookLoaded = isLoaded("nerb");
        isRubidiumLoaded = isLoaded("rubidium");
        isWitherStormModLoaded = isLoaded("witherstormmod");
        isCataclysmLoaded = isLoaded("cataclysm");
        isNeatLoaded = isLoaded("neat");

        if (potatoJsonConfig == null) potatoJsonConfig = new PotatoJsonConfig();
        if (potatoJsonConfig.printModListWhenLaunching) {
            FMLLoader.getLoadingModList().getModFiles().stream()
                    .map(ModFileInfo::getFile)
                    .map(ModFile::getFileName)
                    .sorted()
                    .forEach(name -> LOGGER.info("Mod " + name + " loaded!"));
        }
        instance = this;
        this.onLoad(MIXIN_PACKAGE_ROOT);
        LOGGER.info("Loaded configuration file for Opotato: {} options available, {} override(s) found", config.getOptionCount(), config.getOptionOverrideCount());
    }

    public static boolean isLoaded(String mod) {
        return FMLLoader.getLoadingModList().getModFileById(mod) != null;
    }

    public static void addIncompatibleWarn(@NotNull FMLCommonSetupEvent event, String translationKey) {
        event.enqueueWork(() -> ModLoader.get().addWarning(new ModLoadingWarning(ModLoadingContext.get().getActiveContainer().getModInfo(), ModLoadingStage.COMMON_SETUP, translationKey)));
    }

    @Override
    public void onLoad(String mixinPackage) {
        try {
            config = PotatoMixinConfig.load(new File("./config/opotato/opotato-mixins.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Could not load configuration file for Opotato", e);
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, @NotNull String mixinClassName) {
        if (mixinClassName.endsWith("RenderTypeMixin") && !isLoaded("rubidium") && !isLoaded("embeddium")) {
            return true;
        }

        if (!mixinClassName.startsWith(MIXIN_PACKAGE_ROOT)) {
            LOGGER.error("Expected mixin '{}' to start with package root '{}', treating as foreign and " + "disabling!", mixinClassName, MIXIN_PACKAGE_ROOT);
            return false;
        }

        String mixin = mixinClassName.substring(MIXIN_PACKAGE_ROOT.length());
        Option option = config.getEffectiveOptionForMixin(mixin);
        if (option == null) {
            LOGGER.error("No rules matched mixin '{}', treating as foreign and disabling!", mixin);
            return false;
        }

        if (option.isOverridden()) {
            String source = "[unknown]";
            if (option.isUserDefined()) {
                source = "user configuration";
            }
            if (option.isEnabled()) {
                LOGGER.warn("Force-enabling mixin '{}' as rule '{}' (added by {}) enables it", mixin, option.getName(), source);
            } else {
                LOGGER.warn("Force-disabling mixin '{}' as rule '{}' (added by {}) disables it and children", mixin, option.getName(), source);
            }
        }
        return option.isEnabled();
    }


    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
    @Override public List<String> getMixins() {return null;}
    @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
    @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
package com.teampotato.opotato.mixin;

import com.teampotato.opotato.config.Option;
import com.teampotato.opotato.config.PotatoMixinConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.util.List;
import java.util.Set;

public class PotatoMixinPlugin implements IMixinConfigPlugin {
    private static final String MIXIN_PACKAGE_ROOT = "com.teampotato.opotato.mixin.";
    private final Logger logger = LogManager.getLogger("Opotato");
    public static PotatoMixinConfig config;
    public static PotatoMixinPlugin instance;

    public PotatoMixinPlugin() {
        instance = this;
        try {
            config = PotatoMixinConfig.load(new File("./config/opotato-mixins.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Could not load configuration file for Opotato", e);
        }

        this.logger.info("Loaded configuration file for Opotato: {} options available, {} override(s) found", config.getOptionCount(), config.getOptionOverrideCount());
    }

    @Override
    public void onLoad(String mixinPackage) {
        try {
            config = PotatoMixinConfig.load(new File("./config/opotato-mixins.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Could not load configuration file for Opotato", e);
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!mixinClassName.startsWith(MIXIN_PACKAGE_ROOT)) {
            this.logger.error("Expected mixin '{}' to start with package root '{}', treating as foreign and " + "disabling!", mixinClassName, MIXIN_PACKAGE_ROOT);
            return false;
        }

        String mixin = mixinClassName.substring(MIXIN_PACKAGE_ROOT.length());
        Option option = config.getEffectiveOptionForMixin(mixin);
        if (option == null) {
            this.logger.error("No rules matched mixin '{}', treating as foreign and disabling!", mixin);
            return false;
        }

        if (option.isOverridden()) {
            String source = "[unknown]";
            if (option.isUserDefined()) {
                source = "user configuration";
            } else if (option.isModDefined()) {
                source = "mods [" + String.join(", ", option.getDefiningMods()) + "]";
            }
            if (option.isEnabled()) {
                this.logger.warn("Force-enabling mixin '{}' as rule '{}' (added by {}) enables it", mixin, option.getName(), source);
            } else {
                this.logger.warn("Force-disabling mixin '{}' as rule '{}' (added by {}) disables it and children", mixin, option.getName(), source);
            }
        }
        return option.isEnabled();
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {return null;}

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}

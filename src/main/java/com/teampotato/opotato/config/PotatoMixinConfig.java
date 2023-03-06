package com.teampotato.opotato.config;

import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class PotatoMixinConfig {
    private static final Logger LOGGER = LogManager.getLogger("PotatOptimizeConfig");
    private final Map<String, Option> options = new HashMap<>();

    private PotatoMixinConfig() {
        this.addMixinRule("smoothmenu.SmoothMenuMixin", true);
        this.addMixinRule("mixintrace.MixinCrashReport", true);
        this.addMixinRule("mixintrace.MixinCrashReportSection", true);

        disableIfModPresent("mixin.smoothmenu.SmoothMenuMixin", "forgery");
        disableIfModPresent("mixin.mixintrace.MixinCrashReport", "notenoughcrashes");
        disableIfModPresent("mixin.mixintrace.MixinCrashReportSection", "notenoughcrashes");
    }

    private void disableIfModPresent(String configName, String... ids) {
        for(String id : ids) {
            if(FMLLoader.getLoadingModList().getModFileById(id) != null) {
                this.options.get(configName).addModOverride(false, id);
            }
        }
    }

    private void addMixinRule(String mixin, boolean enabled) {
        String name = getMixinRuleName(mixin);

        if (this.options.putIfAbsent(name, new Option(name, enabled, false)) != null) {
            throw new IllegalStateException("Mixin rule already defined: " + mixin);
        }
    }

    private void readProperties(Properties props) {
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();

            Option option = this.options.get(key);

            if (option == null) {
                LOGGER.warn("No configuration key exists with name '{}', ignoring", key);
                continue;
            }

            boolean enabled;

            if (value.equalsIgnoreCase("true")) {
                enabled = true;
            } else if (value.equalsIgnoreCase("false")) {
                enabled = false;
            } else {
                LOGGER.warn("Invalid value '{}' encountered for configuration key '{}', ignoring", value, key);
                continue;
            }

            option.setEnabled(enabled, true);
        }
    }

    public Option getEffectiveOptionForMixin(String mixinClassName) {
        int lastSplit = 0;
        int nextSplit;

        Option rule = null;

        while ((nextSplit = mixinClassName.indexOf('.', lastSplit)) != -1) {
            String key = getMixinRuleName(mixinClassName.substring(0, nextSplit));

            Option candidate = this.options.get(key);

            if (candidate != null) {
                rule = candidate;

                if (!rule.isEnabled()) {
                    return rule;
                }
            }

            lastSplit = nextSplit + 1;
        }

        return rule;
    }

    public static PotatoMixinConfig load(File file) {
        PotatoMixinConfig config = new PotatoMixinConfig();
        Properties props = new Properties();
        if(file.exists()) {
            try (FileInputStream fin = new FileInputStream(file)){
                props.load(fin);
            } catch (IOException e) {
                throw new RuntimeException("Could not load config file", e);
            }
            config.readProperties(props);
        }

        try {
            config.writeConfig(file, props);
        } catch (IOException e) {
            LOGGER.warn("Could not write configuration file", e);
        }

        return config;
    }

    private void writeConfig(File file, Properties props) throws IOException {
        File dir = file.getParentFile();

        if (!dir.exists()) {
            if (!dir.mkdirs()) throw new IOException("Could not create parent directories");
        } else if (!dir.isDirectory()) {
            throw new IOException("The parent file is not a directory");
        }

        try (Writer writer = new FileWriter(file)) {
            writer.write("# This is the configuration file for ModernFix.\n");
            writer.write("#\n");
            writer.write("# The following options can be enabled or disabled if there is a compatibility issue.\n");
            writer.write("# Add a line mixin.example_name=true/false without the # sign to enable/disable a rule.\n");
            List<String> lines = this.options.keySet().stream()
                    .filter(key -> !key.equals("mixin.core"))
                    .sorted()
                    .map(key -> "#   " + key + "\n")
                    .collect(Collectors.toList());
            for(String line : lines) {
                writer.write(line);
            }
        }
    }

    private static String getMixinRuleName(String name) {
        return "mixin." + name;
    }

    public int getOptionCount() {
        return this.options.size();
    }

    public int getOptionOverrideCount() {
        return (int) this.options.values()
                .stream()
                .filter(Option::isOverridden)
                .count();
    }

    public static class Option {
        private final String name;

        private Set<String> modDefined = null;
        private boolean enabled;
        private boolean userDefined;

        public Option(String name, boolean enabled, boolean userDefined) {
            this.name = name;
            this.enabled = enabled;
            this.userDefined = userDefined;
        }

        public void setEnabled(boolean enabled, boolean userDefined) {
            this.enabled = enabled;
            this.userDefined = userDefined;
        }

        public void addModOverride(boolean enabled, String modId) {
            this.enabled = enabled;

            if (this.modDefined == null) {
                this.modDefined = new LinkedHashSet<>();
            }

            this.modDefined.add(modId);
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public boolean isOverridden() {
            return this.isUserDefined() || this.isModDefined();
        }

        public boolean isUserDefined() {
            return this.userDefined;
        }

        public boolean isModDefined() {
            return this.modDefined != null;
        }

        public String getName() {
            return this.name;
        }

        public void clearModsDefiningValue() {
            this.modDefined = null;
        }

        public Collection<String> getDefiningMods() {
            return this.modDefined != null ? Collections.unmodifiableCollection(this.modDefined) : Collections.emptyList();
        }
    }
}

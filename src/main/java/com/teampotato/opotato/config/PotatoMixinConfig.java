package com.teampotato.opotato.config;

import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class PotatoMixinConfig {
    private static final Logger LOGGER = LogManager.getLogger("OpotatoConfig");
    private final Map<String, Option> options = new HashMap<>();

    private PotatoMixinConfig() {
        this.addMixinRule("smoothmenu", true);
        this.addMixinRule("mixintrace", true);
        this.addMixinRule("alternatecurrent", true);
        this.addMixinRule("betterbeds", false);
        this.addMixinRule("opotato", true);
        this.addMixinRule("kysxis", true);
        disableIfModPresent("mixin.smoothmenu", "forgery");
        disableIfModPresent("mixin.kysxis", "dragora", "ksyxis");
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
            writer.write("# This is the configuration file for Opotato.\n");
            writer.write("#\n");
            writer.write("# The following options can be enabled or disabled if there is a compatibility issue.\n");
            writer.write("# Add a line mixin.example_name=true/false without the # sign to enable/disable a rule.\n");
            writer.write("# Only the mixin.betterbeds is false by default.\n");
            List<String> lines = this.options.keySet().stream().filter(key -> !key.equals("mixin.core")).sorted().map(key -> "#   " + key + "\n").collect(Collectors.toList());
            for(String line : lines) {
                writer.write(line);
            }
            for (Map.Entry<Object, Object> entry : props.entrySet()) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                writer.write(key + "=" + value + "\n");
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
        return (int) this.options.values().stream().filter(Option::isOverridden).count();
    }
}

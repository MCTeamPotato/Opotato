package com.teampotato.opotato.config.mixin;

import com.teampotato.opotato.Opotato;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Map;
import java.util.Properties;

public class PotatoMixinConfig {
    private final Map<String, Option> options = new Object2ObjectOpenHashMap<>();

    private static boolean isLoaded(String mod) {
        return FMLLoader.getLoadingModList().getModFileById(mod) != null;
    }

    private PotatoMixinConfig() {
        this.addMixinRule("opotato", true);
        this.addMixinRule("opotato.arsnouveau", isLoaded("arsnouveau"));
        this.addMixinRule("opotato.blueprint", isLoaded("abnormals_core"));
        this.addMixinRule("opotato.blueskies", isLoaded("blue_skies"));
        this.addMixinRule("opotato.cataclysm", isLoaded("cataclysm"));
        this.addMixinRule("opotato.cataclysm.rubidium", isLoaded("cataclysm") && isLoaded("rubidium"));
        this.addMixinRule("opotato.cataclysm.rubidium.extra", isLoaded("cataclysm") && isLoaded("rubidium") && isLoaded("sodiumextra"));
        this.addMixinRule("opotato.citadel", isLoaded("citadel"));
        this.addMixinRule("opotato.deuf", isLoaded("deuf"));
        this.addMixinRule("opotato.elenaidodge", isLoaded("elenaidodge2"));
        this.addMixinRule("opotato.forge", true);
        this.addMixinRule("opotato.gender", isLoaded("wildfire_gender"));
        this.addMixinRule("opotato.headshot", isLoaded("headshot"));
        this.addMixinRule("opotato.industrialforegoing", isLoaded("industrialforegoing"));
        this.addMixinRule("opotato.itemstages", isLoaded("itemstages"));
        this.addMixinRule("opotato.kiwi", isLoaded("kiwi"));
        this.addMixinRule("opotato.ldlib", isLoaded("ldlib"));
        this.addMixinRule("opotato.modernui", isLoaded("modernui"));
        this.addMixinRule("opotato.placebo", isLoaded("placebo"));
        this.addMixinRule("opotato.quark", isLoaded("quark"));
        this.addMixinRule("opotato.randompatches", isLoaded("randompatches"));
        this.addMixinRule("opotato.spark", isLoaded("spark"));
        this.addMixinRule("opotato.supplementaries", isLoaded("supplementaries"));
        this.addMixinRule("opotato.titanium", isLoaded("titanium"));
        this.addMixinRule("opotato.undergarden", isLoaded("undergarden"));
        this.addMixinRule("opotato.witherstorm", isLoaded("witherstormmod"));
        this.addMixinRule("opotato.xaerominimap", isLoaded("xaerominimap"));
        this.addMixinRule("opotato.xaeroworldmap", isLoaded("xaeroworldmap"));
        this.addMixinRule("alternatecurrent", !isLoaded("potatocurrent"));
        this.addMixinRule("mixintrace", !isLoaded("mixininheaven"));
        this.addMixinRule("api", true);
        this.addMixinRule("api.cataclysm", isLoaded("cataclysm"));
        this.addMixinRule("smoothmenu", !isLoaded("konkrete") && !isLoaded("forgery"));
    }

    private void addMixinRule(String mixin, boolean enabled) {
        String name = getMixinRuleName(mixin);

        if (this.options.putIfAbsent(name, new Option(name, enabled, false)) != null) throw new IllegalStateException("Mixin rule already defined: " + mixin);
    }

    private void readProperties(@NotNull Properties props) {
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();

            Option option = this.options.get(key);

            if (option == null) {
                Opotato.LOGGER.warn("No configuration key exists with name '{}', ignoring", key);
                continue;
            }

            boolean enabled;

            if (value.equalsIgnoreCase("true")) {
                enabled = true;
            } else if (value.equalsIgnoreCase("false")) {
                enabled = false;
            } else {
                Opotato.LOGGER.warn("Invalid value '{}' encountered for configuration key '{}', ignoring", value, key);
                continue;
            }

            option.setEnabled(enabled, true);
        }
    }

    public Option getEffectiveOptionForMixin(@NotNull String mixinClassName) {
        int lastSplit = 0;
        int nextSplit;

        Option rule = null;

        while ((nextSplit = mixinClassName.indexOf('.', lastSplit)) != -1) {
            String key = getMixinRuleName(mixinClassName.substring(0, nextSplit));

            Option candidate = this.options.get(key);

            if (candidate != null) {
                rule = candidate;

                if (!rule.isEnabled()) return rule;
            }

            lastSplit = nextSplit + 1;
        }

        return rule;
    }

    public static @NotNull PotatoMixinConfig load(@NotNull File file) {
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
            Opotato.LOGGER.warn("Could not write configuration file", e);
        }

        return config;
    }

    private void writeConfig(@NotNull File file, Properties props) throws IOException {
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
            writer.write("# All the mixins: \n");
            this.options.keySet().stream().sorted().forEach(line -> {
                try {
                    writer.write("# " + line + "\n");
                } catch (Throwable ignored) {}
            });
            for (Map.Entry<Object, Object> entry : props.entrySet()) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                writer.write(key + "=" + value + "\n");
            }
        }
    }

    @Contract(pure = true)
    private static @NotNull String getMixinRuleName(String name) {
        return "mixin." + name;
    }

    public int getOptionCount() {
        return this.options.size();
    }

    public int getOptionOverrideCount() {
        return (int) this.options.values().stream().filter(Option::isOverridden).count();
    }
}
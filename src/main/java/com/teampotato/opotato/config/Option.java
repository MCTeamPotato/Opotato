package com.teampotato.opotato.config;

import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public class Option {
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

    public boolean isOverridden() {
        return this.isUserDefined() || this.isModDefined();
    }

    public boolean isModDefined() {
        return this.modDefined != null;
    }

    public void clearModsDefiningValue() {
        this.modDefined = null;
    }

    public Collection<String> getDefiningMods() {
        return this.modDefined != null ? Collections.unmodifiableCollection(this.modDefined) : Collections.emptyList();
    }
}
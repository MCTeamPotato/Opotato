package com.teampotato.opotato.config.mixin;

import lombok.Getter;

@Getter
public class Option {
    private final String name;
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

    public boolean isOverridden() {
        return this.isUserDefined();
    }
}
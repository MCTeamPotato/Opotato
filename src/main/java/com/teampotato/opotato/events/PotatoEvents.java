package com.teampotato.opotato.events;

public class PotatoEvents {
    private static final boolean USING_OPTIFINE;

    static {
        boolean hasOfClass = false;
        try {
            Class.forName("optifine.OptiFineTransformationService");
            hasOfClass = true;
        } catch (ClassNotFoundException ignored) {}
        USING_OPTIFINE = hasOfClass;
    }
}

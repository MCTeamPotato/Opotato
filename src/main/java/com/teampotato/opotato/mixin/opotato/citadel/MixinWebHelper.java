package com.teampotato.opotato.mixin.opotato.citadel;

import com.github.alexthe666.citadel.web.WebHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;

@Mixin(value = WebHelper.class, remap = false)
public class MixinWebHelper {
    /**
     * @author Doctor Who
     * @reason Saving The World
     */
    @Overwrite
    @Nullable
    public static BufferedReader getURLContents(@Nonnull String urlString, @Nonnull String backupFileLoc) {
        return null;
    }
}

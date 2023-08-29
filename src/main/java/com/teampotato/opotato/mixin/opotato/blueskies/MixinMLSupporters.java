package com.teampotato.opotato.mixin.opotato.blueskies;

import com.legacy.blue_skies.MLSupporter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = MLSupporter.GetSupportersThread.class, remap = false)
public class MixinMLSupporters {
    /**
     * @author Kasualix
     * @reason no Internet connection behavior
     */
    @Overwrite
    public void run() {}
}

package com.teampotato.opotato.mixin.opotato.elenaidodge;

import com.elenai.elenaidodge2.util.PatronRewardHandler;
import com.teampotato.opotato.Opotato;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = PatronRewardHandler.class, remap = false)
public abstract class MixinPatronRewardHandler {
    @Shadow
    private static Thread thread;

    @Shadow
    private static boolean doneLoading;

    /**
     * @author Kasualix
     * @reason Remove Internet connection behavior
     */
    @Overwrite
    public static void init() {
        if (thread == null || !thread.isAlive()) {
            doneLoading = false;
            thread = new Opotato.EmptyThread();
        }
    }
}

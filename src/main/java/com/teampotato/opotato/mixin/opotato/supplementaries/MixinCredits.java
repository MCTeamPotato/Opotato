package com.teampotato.opotato.mixin.opotato.supplementaries;

import com.teampotato.opotato.Opotato;
import net.mehvahdjukaar.supplementaries.common.Credits;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = Credits.class, remap = false)
public abstract class MixinCredits {
    /**
     * @author Kasualix
     * @reason Remove Internet connection behavior
     */
    @Overwrite
    public static void fetchFromServer() {
        Opotato.LOGGER.info("Opotato: remove supplementaries Internet connection behavior");
        Thread creditsFetcher = new Thread(() -> {});
        creditsFetcher.start();
    }
}

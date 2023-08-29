package com.teampotato.opotato.mixin.opotato.forge;

import net.minecraftforge.fml.loading.FMLConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = FMLConfig.class, remap = false)
public abstract class MixinFMLConfig {
    /**
     * @author Kasualix
     * @reason no more stupid version checker
     */
    @Overwrite
    public static boolean runVersionCheck() {
        return false;
    }
}

package com.teampotato.opotato.mixin.opotato.placebo;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import shadows.placebo.patreon.TrailsManager;

@Mixin(TrailsManager.class)
public class MixinTrailsManager {
    /**
     * @author Doctor Who
     * @reason Saving The World
     */
    @Overwrite
    @SubscribeEvent
    public static void init(FMLClientSetupEvent e) {}
}

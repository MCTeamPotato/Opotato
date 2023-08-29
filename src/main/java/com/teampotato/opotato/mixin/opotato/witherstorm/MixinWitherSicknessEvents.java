package com.teampotato.opotato.mixin.opotato.witherstorm;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.event.WitherSicknessEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = WitherSicknessEvents.class, remap = false)
public abstract class MixinWitherSicknessEvents {
    /**
     * @author Kasualix
     * @reason use another optimized impl
     */
    @Overwrite
    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {}
}

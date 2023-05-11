package com.teampotato.opotato.mixin.opotato.flowingagony;

import love.marblegate.flowingagony.capibility.cooldown.CoolDown;
import love.marblegate.flowingagony.capibility.cooldown.CoolDownType;
import love.marblegate.flowingagony.eventhandler.CoolDownHandler;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Arrays;

@Mixin(value = CoolDownHandler.class, remap = false)
public abstract class MixinCoolDownHandler {
    /**
     * @author Kasualix
     * @reason optimize event
     */
    @Overwrite
    @SubscribeEvent
    public static void handle(TickEvent.PlayerTickEvent event) {
        if (!event.player.level.isClientSide() && event.phase == TickEvent.Phase.START) {
            event.player.getCapability(CoolDown.COOL_DOWN_CAPABILITY)
                    .ifPresent((cap) -> Arrays.stream(CoolDownType.values()).filter(cap::isReady).forEach(cap::decrease));
        }
    }
}

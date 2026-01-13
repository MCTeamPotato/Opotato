package com.teampotato.opotato.mixin.opotato.spiritcallerbackport;

import com.yellowbrossproductions.spiritcallerbackport.events.ModClientEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModClientEvents.class, remap = false)
public abstract class ModClientEventsMixin {
    @Inject(method = {"scareVillagersAway", "stopMobs", "addRaidMembers", "removeRaidMembers", "misconductionAttack1", "misconductionAttack2", "preventGettingHurt"}, at = @At("HEAD"), cancellable = true)
    private static void disable(CallbackInfo ci) {
        ci.cancel();
    }
}

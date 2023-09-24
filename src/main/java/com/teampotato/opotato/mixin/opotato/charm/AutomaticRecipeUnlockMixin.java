package com.teampotato.opotato.mixin.opotato.charm;

import com.teampotato.opotato.Opotato;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.AutomaticRecipeUnlock;

@Mixin(value = AutomaticRecipeUnlock.class, remap = false)
public abstract class AutomaticRecipeUnlockMixin {
    @Inject(method = "onPlayerLoggedIn", at = @At("HEAD"), cancellable = true)
    private void onLogIn(PlayerEvent.PlayerLoggedInEvent event, CallbackInfo ci) {
        if (Opotato.isNotEnoughRecipeBookLoaded) ci.cancel();
    }
}

package com.teampotato.opotato.mixin.opotato.industrialforegoing;

import com.buuz135.industrial.proxy.CommonProxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommonProxy.class)
public abstract class MixinCommonProxy {
    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lcom/hrznstudio/titanium/event/handler/EventManager$FilteredEventManager;subscribe()V", remap = false, shift = At.Shift.AFTER, ordinal = 1), cancellable = true, remap = false)
    private void onConnect(CallbackInfo ci) {
        ci.cancel();
    }
}

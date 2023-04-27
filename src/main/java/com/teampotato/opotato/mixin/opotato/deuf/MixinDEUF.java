package com.teampotato.opotato.mixin.opotato.deuf;

import de.cas_ual_ty.deuf.DEUF;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DEUF.class, remap = false)
public class MixinDEUF {
    @Inject(method = "fix", at = @At("HEAD"), cancellable = true)
    private void noFix(EntityJoinWorldEvent event, CallbackInfo ci) {
        ci.cancel();
    }
}

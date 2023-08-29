package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.entity.effect.Flame_Strike_Entity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinEntity {
    @Inject(method = "updateInWaterStateAndDoFluidPushing", at = @At("HEAD"), cancellable = true)
    private void onUpdate(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity)(Object)this;
        if (entity instanceof Flame_Strike_Entity) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}

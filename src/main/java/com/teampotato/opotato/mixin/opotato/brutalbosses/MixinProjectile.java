package com.teampotato.opotato.mixin.opotato.brutalbosses;

import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Projectile.class, priority = 2000)
public abstract class MixinProjectile {
    @Shadow private boolean leftOwner;
    @Shadow protected abstract boolean checkLeftOwner();

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (!this.leftOwner) {
            this.leftOwner = this.checkLeftOwner();
        }
    }
}

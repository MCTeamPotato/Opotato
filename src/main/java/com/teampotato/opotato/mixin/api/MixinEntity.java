package com.teampotato.opotato.mixin.api;

import com.teampotato.opotato.api.IEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinEntity implements IEntity {
    @Unique
    private boolean potato$shouldMove = true;

    @Override
    public boolean potato$shouldMove() {
        return potato$shouldMove;
    }

    @Override
    public void potato$setShouldMove(boolean shouldMove) {
        this.potato$shouldMove = shouldMove;
    }

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    private void onMove(MoverType type, Vec3 pos, CallbackInfo ci) {
        if (!this.potato$shouldMove()) ci.cancel();
    }
}

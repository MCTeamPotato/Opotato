package com.teampotato.opotato.mixin.nec.client;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.teampotato.opotato.util.nec.StateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BufferBuilder.class)
public abstract class MixinBufferBuilder implements StateManager.IResettable {
    @Shadow
    private boolean building;

    @Shadow
    public abstract void end();

    @Inject(method = "<init>", at = @At("RETURN"))
    public void onInit(int bufferSizeIn, CallbackInfo ci) {
        register();
    }

    @Override
    public void resetState() {
        if (building) {
            end();
        }
    }
}

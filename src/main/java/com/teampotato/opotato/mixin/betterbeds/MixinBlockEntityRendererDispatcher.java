package com.teampotato.opotato.mixin.betterbeds;

import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntityType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(TileEntityRendererDispatcher.class)
public abstract class MixinBlockEntityRendererDispatcher {
    @Shadow
    @Final
    private Map<TileEntityType<?>, TileEntityRenderer<?>> renderers;

    @Inject(method = {"<init>()V"}, at = {@At("TAIL")})
    private void init(CallbackInfo info) {
        this.renderers.remove(TileEntityType.BED);
    }
}

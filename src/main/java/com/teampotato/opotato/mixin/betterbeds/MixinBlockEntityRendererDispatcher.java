package com.teampotato.opotato.mixin.betterbeds;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(BlockEntityRenderDispatcher.class)
public abstract class MixinBlockEntityRendererDispatcher {
    @Shadow
    @Final
    private Map<BlockEntityType<?>, BlockEntityRenderer<?>> renderers;

    @Inject(method = {"<init>()V"}, at = {@At("TAIL")})
    private void init(CallbackInfo info) {
        this.renderers.remove(BlockEntityType.BED);
    }
}

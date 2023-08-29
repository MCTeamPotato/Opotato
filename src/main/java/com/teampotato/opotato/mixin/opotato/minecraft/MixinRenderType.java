package com.teampotato.opotato.mixin.opotato.minecraft;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(RenderType.class)
public abstract class MixinRenderType {
    @Unique
    private static final List<RenderType> renderType_cache = ImmutableList.of(RenderType.solid(), RenderType.cutoutMipped(), RenderType.cutout(), RenderType.translucent(), RenderType.tripwire());

    /**
     * @author Kasualix
     * @reason impl cache
     */
    @Overwrite
    public static List<RenderType> chunkBufferLayers() {
        return renderType_cache;
    }
}

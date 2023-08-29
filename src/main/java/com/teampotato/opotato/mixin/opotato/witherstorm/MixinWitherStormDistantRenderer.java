package com.teampotato.opotato.mixin.opotato.witherstorm;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import nonamecrackers2.witherstormmod.client.capability.WitherStormDistantRenderer;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = WitherStormDistantRenderer.class, remap = false)
public class MixinWitherStormDistantRenderer {
    @Shadow @Final private Int2ObjectMap<WitherStormEntity> stormsToRender;

    /**
     * @author Kasualix
     * @reason remove unmodifiable
     */
    @Overwrite
    public Iterable<WitherStormEntity> getKnown() {
        return this.stormsToRender.values();
    }
}

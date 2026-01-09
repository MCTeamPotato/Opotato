package com.teampotato.opotato.mixin.opotato.astralsorcery;

import hellfirepvp.astralsorcery.common.util.CacheEventBus;
import net.minecraftforge.eventbus.api.IEventBus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = CacheEventBus.class, remap = false)
public abstract class MixinCacheEventBus {
    @Shadow @Final private IEventBus wrapped;
    @Shadow @Final private List<Object> registeredListeners;

    /**
     * @author Kall
     * @reason Fix ArrayIndexOutOfBoundsException
     */
    @Overwrite
    public void unregisterAll() {
        List<Object> copy = new ArrayList<>(this.registeredListeners);
        copy.forEach(this.wrapped::unregister);
        this.registeredListeners.clear();
    }
}

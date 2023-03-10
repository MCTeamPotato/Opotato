package com.teampotato.opotato.mixin.nec.client;

import com.teampotato.opotato.util.nec.mixinhandlers.EntryPointCatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z"))
    private boolean redirectHasNext(Iterator iterator) {
        if (EntryPointCatcher.crashedDuringStartup()) {
            return false;
        } else {
            return iterator.hasNext();
        }
    }
}

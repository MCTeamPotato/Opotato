package com.teampotato.opotato.mixin.opotato.deuf;

import de.cas_ual_ty.deuf.DEUF;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;

@Mixin(value = DEUF.class, remap = false)
public abstract class MixinDEUF {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/eventbus/api/IEventBus;addListener(Ljava/util/function/Consumer;)V", remap = false))
    private void noFix(IEventBus instance, Consumer<ChunkEvent.Load> tConsumer) {}
}

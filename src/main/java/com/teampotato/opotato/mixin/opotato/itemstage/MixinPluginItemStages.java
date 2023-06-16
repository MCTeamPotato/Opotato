package com.teampotato.opotato.mixin.opotato.itemstage;

import mezz.jei.api.runtime.IJeiRuntime;
import net.darkhax.itemstages.jei.PluginItemStages;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(value = PluginItemStages.class, remap = false)
public abstract class MixinPluginItemStages {
    @Shadow protected abstract void updateHiddenItems();

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/eventbus/api/IEventBus;addListener(Lnet/minecraftforge/eventbus/api/EventPriority;ZLjava/lang/Class;Ljava/util/function/Consumer;)V", ordinal = 1))
    private void onListen(IEventBus instance, EventPriority eventPriority, boolean b, Class tClass, Consumer tConsumer) {}

    @Inject(method = "onRuntimeAvailable", at = @At("HEAD"))
    private void onAvailable(IJeiRuntime jeiRuntime, CallbackInfo ci) {
        this.updateHiddenItems();
    }
}

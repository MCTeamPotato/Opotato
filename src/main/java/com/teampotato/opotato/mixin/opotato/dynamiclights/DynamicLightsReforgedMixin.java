package com.teampotato.opotato.mixin.opotato.dynamiclights;

import io.netty.util.internal.ConcurrentSet;
import me.lambdaurora.lambdynlights.DynamicLightSource;
import me.lambdaurora.lambdynlights.DynamicLightsReforged;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(DynamicLightsReforged.class)
public abstract class DynamicLightsReforgedMixin {
    @Mutable @Shadow(remap = false) @Final private static Set<DynamicLightSource> dynamicLightSources;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void concurrentSetInit(CallbackInfo ci) {
        if (dynamicLightSources.isEmpty()) {
            dynamicLightSources = new ConcurrentSet<>();
        } else {
            final Set<DynamicLightSource> dynamicLightSourceSet = new ConcurrentSet<>();
            dynamicLightSourceSet.addAll(dynamicLightSources);
            dynamicLightSources = dynamicLightSourceSet;
        }
    }
}

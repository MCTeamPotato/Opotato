package com.teampotato.opotato.mixin.mixintrace;

import com.teampotato.opotato.util.mixintrace.TraceUtils;
import net.minecraft.CrashReportCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CrashReportCategory.class)
public abstract class MixinCrashReportSection {
    @Shadow private StackTraceElement[] stackTrace;

    @Inject(method = "getDetails", at = @At("TAIL"))
    private void mixinTrace_addTrace(StringBuilder crashReportBuilder, CallbackInfo ci) {
        TraceUtils.printTrace(stackTrace, crashReportBuilder);
    }
}
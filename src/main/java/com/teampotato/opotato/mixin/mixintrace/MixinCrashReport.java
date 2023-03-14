package com.teampotato.opotato.mixin.mixintrace;

import com.teampotato.opotato.util.TraceUtils;
import net.minecraft.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CrashReport.class)
public abstract class MixinCrashReport {
    @Shadow
    private StackTraceElement[] uncategorizedStackTrace;

    @Inject(method = "getDetails", at = @At(value = "FIELD", target = "Lnet/minecraft/crash/CrashReport;details:Ljava/util/List;"))
    private void mixintrace_addTrace(StringBuilder crashReportBuilder, CallbackInfo ci) {
        int trailingNewlineCount = 0;
        if (crashReportBuilder.charAt(crashReportBuilder.length() - 1) == '\n') {
            crashReportBuilder.deleteCharAt(crashReportBuilder.length() - 1);
            trailingNewlineCount++;
        }
        if (crashReportBuilder.charAt(crashReportBuilder.length() - 1) == '\n') {
            crashReportBuilder.deleteCharAt(crashReportBuilder.length() - 1);
            trailingNewlineCount++;
        }
        TraceUtils.printTrace(uncategorizedStackTrace, crashReportBuilder);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < trailingNewlineCount; i++) {
            sb.append("\n");
        }
        crashReportBuilder.append(sb);
    }
}
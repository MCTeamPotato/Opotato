package com.teampotato.opotato.mixin.mixintrace;

import com.teampotato.opotato.util.mixintrace.TraceUtils;
import net.minecraft.CrashReport;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CrashReport.class)
public abstract class MixinCrashReport {

    @Shadow private StackTraceElement[] uncategorizedStackTrace;

    @Inject(method = "getDetails", at = @At(value = "FIELD", target = "Lnet/minecraft/CrashReport;details:Ljava/util/List;"))
    private void mixinTrace_addTrace(@NotNull StringBuilder crashReportBuilder, CallbackInfo ci) {
        int trailingNewlineCount = 0;
        // Remove trailing \n
        if (crashReportBuilder.charAt(crashReportBuilder.length() - 1) == '\n') {
            crashReportBuilder.deleteCharAt(crashReportBuilder.length() - 1);
            trailingNewlineCount++;
        }
        if (crashReportBuilder.charAt(crashReportBuilder.length() - 1) == '\n') {
            crashReportBuilder.deleteCharAt(crashReportBuilder.length() - 1);
            trailingNewlineCount++;
        }
        TraceUtils.printTrace(uncategorizedStackTrace, crashReportBuilder);
        crashReportBuilder.append(StringUtils.repeat("\n", trailingNewlineCount));
    }
}

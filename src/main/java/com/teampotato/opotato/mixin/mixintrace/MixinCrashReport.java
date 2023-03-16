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
    private void mixintrace_addTrace(StringBuilder p_71506_1_, CallbackInfo ci) {
        int trailingNewlineCount = 0;
        if (p_71506_1_.charAt(p_71506_1_.length() - 1) == '\n') {
            p_71506_1_.deleteCharAt(p_71506_1_.length() - 1);
            trailingNewlineCount++;
        }
        if (p_71506_1_.charAt(p_71506_1_.length() - 1) == '\n') {
            p_71506_1_.deleteCharAt(p_71506_1_.length() - 1);
            trailingNewlineCount++;
        }
        TraceUtils.printTrace(uncategorizedStackTrace, p_71506_1_);
        p_71506_1_.append("\n".repeat(trailingNewlineCount));
    }
}
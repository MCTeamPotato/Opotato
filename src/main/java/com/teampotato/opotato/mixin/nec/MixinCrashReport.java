package com.teampotato.opotato.mixin.nec;

import com.teampotato.opotato.util.nec.platform.CommonModMetadata;
import com.teampotato.opotato.util.nec.stacktrace.ModIdentifier;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.stream.Collectors;


@Mixin(value = CrashReport.class, priority = 500)
public abstract class MixinCrashReport {

    @Shadow
    @Final
    private CrashReportCategory systemDetails;

    private CrashReport getThis() {
        return (CrashReport) (Object) this;
    }

    @Inject(method = "initDetails", at = @At("TAIL"))
    private void afterInitDetails(CallbackInfo ci) {
        systemDetails.setDetail("Suspected Mods", () -> {
            try {
                Set<CommonModMetadata> suspectedMods = ModIdentifier.getSuspectedModsOf(getThis());
                if (!suspectedMods.isEmpty()) {
                    return suspectedMods.stream()
                            .map((mod) -> mod.name() + " (" + mod.id() + ")")
                            .collect(Collectors.joining(", "));
                } else return "None";
            } catch (Throwable e) {
                return ExceptionUtils.getStackTrace(e).replace("\t", "    ");
            }
        });
    }
}
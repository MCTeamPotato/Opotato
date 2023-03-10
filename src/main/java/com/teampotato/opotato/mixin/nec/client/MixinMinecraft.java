package com.teampotato.opotato.mixin.nec.client;

import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.util.nec.mixinhandlers.EntryPointCatcher;
import com.teampotato.opotato.util.nec.mixinhandlers.InGameCatcher;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft extends ReentrantBlockableEventLoop<Runnable> {
    @Shadow
    private CrashReport crashReport;

    @Shadow
    @Final
    private Queue<Runnable> renderTaskQueue;

    public MixinMinecraft(String string_1) {
        super(string_1);
    }

    @Inject(method = "run()V", at = @At("HEAD"))
    private void beforeRun(CallbackInfo ci) {
        if (EntryPointCatcher.crashedDuringStartup()) EntryPointCatcher.displayInitErrorScreen();
    }

    @Inject(method = "run()V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;crashReport:Lnet/minecraft/util/crash/CrashReport;"))
    private void onRunLoop(CallbackInfo ci) {
        if (!Opotato.ENABLE_GAMELOOP_CATCHING) return;

        if (this.crashReport != null) {
            Opotato.LOGGER.debug("Handling run loop crash");
            InGameCatcher.handleServerCrash(crashReport);

            // Causes the run loop to keep going
            crashReport = null;
        }
    }


    // Can't capture arg in inject so captured here
    @ModifyArg(method = "run()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;printCrashReport(Lnet/minecraft/util/crash/CrashReport;)V", ordinal = 1))
    private CrashReport atTheEndOfFirstCatchBeforePrintingCrashReport(CrashReport report) {
        if (!Opotato.ENABLE_GAMELOOP_CATCHING) return report;

        Opotato.LOGGER.debug("Handling client game loop try/catch crash in first catch block");
        // we MUST use the report passed as parameter, because the field one only gets assigned in integrated server crashes.
        InGameCatcher.handleClientCrash(report);
        return report;
    }

    // Can't capture arg in inject so captured here
    @ModifyArg(method = "run()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;printCrashReport(Lnet/minecraft/util/crash/CrashReport;)V", ordinal = 2))
    private CrashReport atTheEndOfSecondCatchBeforePrintingCrashReport(CrashReport report) {
        if (!Opotato.ENABLE_GAMELOOP_CATCHING) return report;

        Opotato.LOGGER.debug("Handling client game loop try/catch crash in second catch block");
        // we MUST use the report passed as parameter, because the field one only gets assigned in integrated server crashes.
        InGameCatcher.handleClientCrash(report);
        return report;
    }

    // Prevent calling printCrashReport which is not needed
    @Inject(method = "run()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;printCrashReport(Lnet/minecraft/util/crash/CrashReport;)V"), cancellable = true)
    private void cancelRunLoopAfterCrash(CallbackInfo ci) {
        if (Opotato.ENABLE_GAMELOOP_CATCHING) ci.cancel();
    }

    @Inject(method = "emergencySave()V", at = @At("HEAD"))
    private void beforeEmergencySave(CallbackInfo info) {
        InGameCatcher.cleanupBeforeMinecraft(renderTaskQueue);
    }

    /**
     * Prevent the integrated server from exiting in the case it crashed
     */
    @Redirect(method = "doLoadLevel(Ljava/lang/String;Lnet/minecraft/util/registry/DynamicRegistryManager$Impl;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function4;ZLnet/minecraft/client/MinecraftClient$WorldLoadAction;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;printCrashReport(Lnet/minecraft/util/crash/CrashReport;)V"))
    private void redirectPrintCrashReport(CrashReport report) {
    }
    /**
     * Forge only: Prevent the integrated server from exiting in the case it crashed in another case
     */
    @Redirect(method = "loadLevel(Ljava/lang/String;Lnet/minecraft/util/registry/DynamicRegistryManager$Impl;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function4;ZLnet/minecraft/client/MinecraftClient$WorldLoadAction;Z)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;printCrashReport(Lnet/minecraft/util/crash/CrashReport;)V"),
            require = 0)
    private void redirectForgePrintCrashReport(CrashReport report) {
    }

}
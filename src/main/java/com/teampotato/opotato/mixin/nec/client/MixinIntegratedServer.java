package com.teampotato.opotato.mixin.nec.client;

import com.teampotato.opotato.access.PatchedIntegratedServer;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.client.server.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IntegratedServer.class)
public class MixinIntegratedServer implements PatchedIntegratedServer {

    private boolean crashNextTick = false;

    @Override
    public void setCrashNextTick() {
        crashNextTick = true;
    }

    @Inject(method = "tickServer", at = @At("HEAD"))
    private void beforeTick(CallbackInfo ci) {
        if (crashNextTick) {
            throw new ReportedException(new CrashReport("Manually triggered server-side debug crash", new Throwable()));
        }
    }
}
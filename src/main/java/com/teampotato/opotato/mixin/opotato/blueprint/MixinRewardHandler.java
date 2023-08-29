package com.teampotato.opotato.mixin.opotato.blueprint;

import com.minecraftabnormals.abnormals_core.client.RewardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

@Mixin(value = RewardHandler.class, remap = false)
public class MixinRewardHandler {
    @Redirect(method = "clientSetup", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/CompletableFuture;thenAcceptAsync(Ljava/util/function/Consumer;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"))
    private static CompletableFuture<Void> onClientSetup(CompletableFuture<?> instance, Consumer<?> action, Executor executor) {
        return null;
    }
}

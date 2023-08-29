package com.teampotato.opotato.mixin.opotato.titanium;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.reward.Reward;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.URL;
import java.util.List;
import java.util.UUID;

@Mixin(value = Reward.class, remap = false)
public abstract class MixinReward {
    @Inject(method = "getPlayers(Ljava/net/URL;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
    private static void onGetPlayers(URL url, CallbackInfoReturnable<List<UUID>> cir) {
        cir.setReturnValue(Lists.newArrayList());
        cir.cancel();
    }
}

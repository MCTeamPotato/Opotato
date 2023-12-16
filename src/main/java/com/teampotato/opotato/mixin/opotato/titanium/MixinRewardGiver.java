package com.teampotato.opotato.mixin.opotato.titanium;

import com.hrznstudio.titanium.reward.Reward;
import com.hrznstudio.titanium.reward.RewardGiver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RewardGiver.class)
public abstract class MixinRewardGiver {
    @Inject(method = "addReward", remap = false, at = @At("HEAD"), cancellable = true)
    private void onAddReward(Reward reward, CallbackInfo ci) {
        ci.cancel();
    }
}

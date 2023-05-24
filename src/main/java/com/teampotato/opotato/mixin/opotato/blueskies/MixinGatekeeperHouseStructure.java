package com.teampotato.opotato.mixin.opotato.blueskies;

import com.legacy.blue_skies.world.general_features.structures.GatekeeperHouseStructure;
import com.teampotato.opotato.config.PotatoCommonConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GatekeeperHouseStructure.class, remap = false)
public abstract class MixinGatekeeperHouseStructure {

    @Inject(method = "getSpacing", at = @At("HEAD"), cancellable = true)
    private void getSpacing(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(PotatoCommonConfig.GATE_KEEPER_HOUSE_SPACING.get());
    }
}

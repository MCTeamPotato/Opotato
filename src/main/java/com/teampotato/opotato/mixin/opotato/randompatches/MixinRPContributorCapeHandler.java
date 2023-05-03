package com.teampotato.opotato.mixin.opotato.randompatches;

import com.teampotato.opotato.Opotato;
import com.therandomlabs.randompatches.client.RPContributorCapeHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = RPContributorCapeHandler.class, remap = false)
public class MixinRPContributorCapeHandler {
    @Redirect(method = "onPreRenderPlayer", at = @At(value = "INVOKE", target = "Lcom/therandomlabs/randompatches/client/RPContributorCapeHandler;downloadContributorList()V"))
    private static void onDownload() {}
}

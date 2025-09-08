package com.teampotato.opotato.mixin.opotato.blueskies;

import com.teampotato.opotato.config.mods.BlueSkiesExtraConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LightTexture.class)
public abstract class MixinMCLightTextureForGamma {
    @ModifyArg(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Lcom/mojang/math/Vector3f;lerp(Lcom/mojang/math/Vector3f;F)V", ordinal = 5))
    private float onUseGamma(float gamma) {
        return BlueSkiesExtraConfig.lockGamma.get() ? gamma : (float) Minecraft.getInstance().options.gamma;
    }
}

package com.teampotato.opotato.mixin.opotato.blueskies;

import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class)
public abstract class MixinMinecraftLivingEntity {
    @Shadow
    public abstract double getAttributeValue(Attribute attribute);

    @Inject(at = @At("HEAD"), method = "getArmorValue", cancellable = true)
    private void getArmorValue(CallbackInfoReturnable<Integer> callback) {
        if (!PotatoCommonConfig.ENABLE_BLUE_SKIES_NERF.get()) {
            callback.setReturnValue(MathHelper.floor(this.getAttributeValue(Attributes.ARMOR)));
            callback.cancel();
        }
    }
}

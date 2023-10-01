package com.teampotato.opotato.mixin.opotato.blueskies;

import com.teampotato.opotato.config.mods.BlueSkiesExtraConfig;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinMCLivingEntity {
    @Shadow public abstract double getAttributeValue(Attribute attribute);

    @Inject(method = "getArmorValue", at = @At("HEAD"), cancellable = true)
    private void onGetArmorValue(CallbackInfoReturnable<Integer> cir) {
        if (BlueSkiesExtraConfig.enableDimensionalNerf.get()) return;
        cir.setReturnValue(Mth.floor(this.getAttributeValue(Attributes.ARMOR)));
    }
}

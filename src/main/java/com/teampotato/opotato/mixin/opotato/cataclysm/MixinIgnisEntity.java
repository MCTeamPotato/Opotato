package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.entity.Ignis_Entity;
import com.teampotato.opotato.config.mods.CataclysmExtraConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Ignis_Entity.class)
public abstract class MixinIgnisEntity {
    @ModifyConstant(method = "aiStep", constant = @Constant(floatValue =  2.0F, ordinal = 22))
    private float modifyRadius1(float constant) {
        return CataclysmExtraConfig.flameStrikeSummonedByIgnisUltimateAttackRadius.get().floatValue();
    }

    @ModifyConstant(method = "aiStep", constant = @Constant(floatValue =  2.0F, ordinal = 23))
    private float modifyRadius2(float constant) {
        return CataclysmExtraConfig.flameStrikeSummonedByIgnisUltimateAttackRadius.get().floatValue();
    }
}

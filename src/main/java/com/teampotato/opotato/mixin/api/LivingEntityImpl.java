package com.teampotato.opotato.mixin.api;

import com.teampotato.opotato.api.ExtendedLivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class LivingEntityImpl implements ExtendedLivingEntity {
    @Shadow protected abstract boolean isDamageSourceBlocked(DamageSource pDamageSource);

    @Override
    public boolean opotato$isDamageSourceBlocked(DamageSource pDamageSource) {
        return this.isDamageSourceBlocked(pDamageSource);
    }
}

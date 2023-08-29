package com.teampotato.opotato.mixin.opotato.minecraft;

import com.teampotato.opotato.api.ILivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity implements ILivingEntity {
    @Shadow protected abstract boolean isDamageSourceBlocked(DamageSource arg);

    @Override
    public boolean potato$isDamageSourceBlocked(DamageSource arg) {
        return this.isDamageSourceBlocked(arg);
    }
}

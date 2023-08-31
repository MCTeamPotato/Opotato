package com.teampotato.opotato.mixin.api.cataclysm;

import L_Ender.cataclysm.entity.effect.Flame_Strike_Entity;
import com.teampotato.opotato.api.IFlameStrikeEntity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Flame_Strike_Entity.class)
public abstract class MixinFlameStrikeEntity implements IFlameStrikeEntity {
    @Shadow protected abstract void damage(LivingEntity entity);

    @Override
    public void damagePublic(LivingEntity entity) {
        this.damage(entity);
    }
}

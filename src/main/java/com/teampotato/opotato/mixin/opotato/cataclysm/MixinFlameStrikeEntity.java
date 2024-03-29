package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.entity.effect.Flame_Strike_Entity;
import L_Ender.cataclysm.init.ModEffect;
import com.teampotato.opotato.api.entity.LightestEntity;
import com.teampotato.opotato.config.mods.CataclysmExtraConfig;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(Flame_Strike_Entity.class)
public abstract class MixinFlameStrikeEntity extends Entity implements LightestEntity {

    @Shadow @Nullable public abstract LivingEntity getOwner();

    @Shadow(remap = false) public abstract boolean isSoul();

    public MixinFlameStrikeEntity(EntityType<?> arg, Level arg2) {
        super(arg, arg2);
    }

    /**
     * @author Kasualix
     * @reason impl config
     */
    @Overwrite(remap = false)
    private void damage(@NotNull LivingEntity hitEntity) {
        LivingEntity owner = this.getOwner();
        final MobEffect blazingBrand = ModEffect.EFFECTBLAZING_BRAND.get();
        if (!hitEntity.isAlive() || hitEntity.isInvulnerable() || hitEntity == owner || tickCount % 2 != 0) return;
        float entityDamage = this.isSoul() ? CataclysmExtraConfig.flameStrikeSoulDamage.get().floatValue() : CataclysmExtraConfig.flameStrikeNormalDamage.get().floatValue();
        if (owner == null) {
            boolean flag = hitEntity.hurt(DamageSource.MAGIC, entityDamage + hitEntity.getMaxHealth() * CataclysmExtraConfig.flameStrikeNormalPercentageDamage.get().floatValue());
            if (flag) {
                MobEffectInstance blazingBrandEffect = hitEntity.getEffect(blazingBrand);
                int i = 1;
                if (blazingBrandEffect != null) {
                    i += blazingBrandEffect.getAmplifier();
                    hitEntity.removeEffectNoUpdate(blazingBrand);
                } else {
                    --i;
                }

                i = Mth.clamp(i, 0, 4);
                hitEntity.addEffect(new MobEffectInstance(blazingBrand, CataclysmExtraConfig.blazingBrandDurationOnFlameStrike.get(), i, false, false, true));
            }
        } else if (!owner.isAlliedTo(hitEntity)) {
            float maxHealthPercentage = owner instanceof Player ? CataclysmExtraConfig.flameStrikePlayerPercentageDamage.get().floatValue() : CataclysmExtraConfig.flameStrikeNormalPercentageDamage.get().floatValue();
            boolean flag = hitEntity.hurt(DamageSource.indirectMagic(this, owner), entityDamage + hitEntity.getMaxHealth() * maxHealthPercentage);
            if (flag) {
                MobEffectInstance blazingBrandEffect = hitEntity.getEffect(blazingBrand);
                int i = 1;
                if (blazingBrandEffect != null) {
                    i += blazingBrandEffect.getAmplifier();
                    hitEntity.removeEffectNoUpdate(blazingBrand);
                } else {
                    --i;
                }
                i = Mth.clamp(i, 0, 4);
                hitEntity.addEffect(new MobEffectInstance(blazingBrand, CataclysmExtraConfig.blazingBrandDurationOnFlameStrike.get(), i, false, false, true));
            }
        }
    }
}

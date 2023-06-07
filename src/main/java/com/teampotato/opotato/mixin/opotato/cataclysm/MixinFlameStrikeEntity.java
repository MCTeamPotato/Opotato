package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.entity.effect.Flame_Strike_Entity;
import L_Ender.cataclysm.init.ModEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

import static com.teampotato.opotato.config.PotatoCommonConfig.*;

@Mixin(value = Flame_Strike_Entity.class, remap = false)
public abstract class MixinFlameStrikeEntity extends Entity {
    public MixinFlameStrikeEntity(EntityType<?> pType, World pLevel) {
        super(pType, pLevel);
    }

    @Shadow @Nullable public abstract LivingEntity getOwner();
    @Shadow public abstract boolean isSoul();

    /**
     * @author Kasualix
     * @reason impl config
     */
    @Overwrite
    private void damage(LivingEntity attacked) {
        LivingEntity attacker = this.getOwner();
        float maxHealthOfTheAttacked = attacked.getMaxHealth();
        if (attacked.isAlive() && !attacked.isInvulnerable() && attacked != attacker && this.tickCount % 2 == 0) {
            if (attacker == null) {
                if (attacked.hurt(DamageSource.MAGIC, (float) ((this.isSoul() ? 8.0F : 6.0F) + maxHealthOfTheAttacked * 0.06))) {
                    EffectInstance blazingBrand = attacked.getEffect(BLAZING_BRAND);
                    int i = 1;
                    if (blazingBrand != null) {
                        i += blazingBrand.getAmplifier();
                        attacked.removeEffectNoUpdate(BLAZING_BRAND);
                    } else {
                        --i;
                    }

                    i = MathHelper.clamp(i, 0, 4);
                    attacked.addEffect(new EffectInstance(BLAZING_BRAND, 200, i, false, false, true));
                }
            } else {
                boolean isPlayerAttack = attacker instanceof PlayerEntity;
                if (attacker.isAlliedTo(attacked)) return;
                float damage;
                if (isPlayerAttack) {
                    damage = FLAME_STRIKE_OF_INCINERATOR_BASIC_DAMAGE.get();
                } else {
                    damage = this.isSoul() ? 8.0F : 6.0F;
                }

                if (attacked.hurt(DamageSource.indirectMagic(this, attacker), (float) (damage + maxHealthOfTheAttacked * (isPlayerAttack ? FLAME_STRIKE_EXTRA_DAMAGE_PERCENT.get() : 0.06)))) {
                    EffectInstance blazingBrand = attacked.getEffect(BLAZING_BRAND);
                    int i = 1;
                    if (blazingBrand != null) {
                        i += blazingBrand.getAmplifier();
                        attacked.removeEffectNoUpdate(BLAZING_BRAND);
                    } else {
                        --i;
                    }
                    i = MathHelper.clamp(i, 0, 4);
                    if (isPlayerAttack) {
                        attacked.addEffect(new EffectInstance(BLAZING_BRAND, BLAZING_BRAND_EFFECT_DURATION_ON_FLAME_STRIKE.get(), i, false, false, true));
                    } else {
                        attacked.addEffect(new EffectInstance(BLAZING_BRAND, 200, i, false, false, true));
                    }
                }
            }
        }
    }

    private static final Effect BLAZING_BRAND = ModEffect.EFFECTBLAZING_BRAND.get();
}

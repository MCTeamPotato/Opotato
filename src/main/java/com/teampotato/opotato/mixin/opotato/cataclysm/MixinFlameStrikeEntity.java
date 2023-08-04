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
import org.spongepowered.asm.mixin.Unique;

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
    private void damage(LivingEntity Hitentity) {
        LivingEntity caster = this.getOwner();
        boolean playerIsTheCaster = caster instanceof PlayerEntity;
        if (Hitentity.isAlive() && !Hitentity.isInvulnerable() && Hitentity != caster && this.tickCount % 2 == 0) {
            float damage = playerIsTheCaster ? FLAME_STRIKE_OF_INCINERATOR_BASIC_DAMAGE.get() : (this.isSoul() ? 8.0F : 6.0F);
            if (caster == null) {
                boolean flag = Hitentity.hurt(DamageSource.MAGIC, damage + Hitentity.getMaxHealth() * 0.06F);
                if (flag) {
                    EffectInstance effectinstance1 = Hitentity.getEffect(BLAZING_BRAND);
                    int i = 1;
                    if (effectinstance1 != null) {
                        i += effectinstance1.getAmplifier();
                        Hitentity.removeEffectNoUpdate(BLAZING_BRAND);
                    } else {
                        --i;
                    }

                    i = MathHelper.clamp(i, 0, 4);
                    EffectInstance effectinstance = new EffectInstance(BLAZING_BRAND, 200, i, false, false, true);
                    Hitentity.addEffect(effectinstance);
                }
            } else {
                float hpDmg = (float)(playerIsTheCaster ? FLAME_STRIKE_EXTRA_DAMAGE_PERCENT.get() : 0.06);
                if (caster.isAlliedTo(Hitentity)) {
                    return;
                }

                boolean flag = Hitentity.hurt(DamageSource.indirectMagic(this, caster), damage + Hitentity.getMaxHealth() * hpDmg);
                if (flag) {
                    EffectInstance effectinstance1 = Hitentity.getEffect(BLAZING_BRAND);
                    int i = 1;
                    if (effectinstance1 != null) {
                        i += effectinstance1.getAmplifier();
                        Hitentity.removeEffectNoUpdate(BLAZING_BRAND);
                    } else {
                        --i;
                    }

                    i = MathHelper.clamp(i, 0, 4);
                    EffectInstance effectinstance = new EffectInstance(BLAZING_BRAND, playerIsTheCaster ? BLAZING_BRAND_EFFECT_DURATION_ON_FLAME_STRIKE.get() : 200, i, false, false, true);
                    Hitentity.addEffect(effectinstance);
                }
            }
        }
    }

    @Unique
    private static final Effect BLAZING_BRAND = ModEffect.EFFECTBLAZING_BRAND.get();
}

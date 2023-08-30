package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.entity.effect.Flame_Strike_Entity;
import L_Ender.cataclysm.init.ModEffect;
import L_Ender.cataclysm.init.ModEntities;
import com.teampotato.opotato.config.mods.CataclysmExtraConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Mixin(value = Flame_Strike_Entity.class, remap = false)
public abstract class MixinFlameStrikeEntity extends Entity {
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;", shift = At.Shift.BEFORE), cancellable = true, remap = true)
    private void onTick(CallbackInfo ci) {
        if (this.level.isClientSide) ci.cancel();
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextGaussian()D", remap = false), remap = true)
    private double onGetGaussian(Random instance) {
        return gaussianList.get(ThreadLocalRandom.current().nextInt(gaussianList.size()));
    }

    /**
     * @author Kasualix
     * @reason cache radius to avoid the additional overhead in getEntityData.
     */
    @Overwrite
    public float getRadius() {
        if (this.isPlayerTheOwner) {
            return CataclysmExtraConfig.flameStrikeSummonedByIncineratorRadius.get().floatValue();
        } else if (this.isIgnisTheOwner) {
           return CataclysmExtraConfig.flameStrikeSummonedByIgnisUltimateAttackRadius.get().floatValue();
        } else {
            return this.getEntityData().get(DATA_RADIUS);
        }
    }

    @Unique
    private static final ObjectArrayList<Double> gaussianList = new ObjectArrayList<>();

    static {
        for (int i = 0; i < 10; i++) {
            double randomGaussian = ThreadLocalRandom.current().nextGaussian();
            gaussianList.add(randomGaussian);
        }
    }

    @Shadow @Nullable public abstract LivingEntity getOwner();

    /**
     * @author Kasualix
     * @reason impl cache
     */
    @Overwrite
    public boolean isSoul() {
        return this.isSoul;
    }

    @Shadow @Final private static EntityDataAccessor<Float> DATA_RADIUS;

    public MixinFlameStrikeEntity(EntityType<?> arg, Level arg2) {
        super(arg, arg2);
    }

    /**
     * @author Kasualix
     * @reason impl config
     */
    @Overwrite
    private void damage(LivingEntity hitEntity) {
        LivingEntity owner = this.getOwner();
        final MobEffect blazingBrand = ModEffect.EFFECTBLAZING_BRAND.get();
        if (!hitEntity.isAlive() || hitEntity.isInvulnerable() || hitEntity == owner || tickCount % 2 != 0) return;
        float entityDamage = this.isSoul() ?
                CataclysmExtraConfig.flameStrikeSoulDamage.get().floatValue() :
                CataclysmExtraConfig.flameStrikeNormalDamage.get().floatValue();
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

    @Unique
    private boolean isPlayerTheOwner;
    @Unique
    private boolean isIgnisTheOwner;
    @Unique
    private boolean isSoul;

    @Inject(method = "setOwner", at = @At("HEAD"))
    private void onSetOwner(LivingEntity ownerIn, CallbackInfo ci) {
        this.isIgnisTheOwner = ownerIn.getType().equals(ModEntities.IGNIS.get());
        this.isPlayerTheOwner = ownerIn instanceof Player;
    }

    @Inject(method = "setSoul", at = @At("HEAD"))
    private void onSetSoul(boolean Soul, CallbackInfo ci) {
        this.isSoul = Soul;
    }
}

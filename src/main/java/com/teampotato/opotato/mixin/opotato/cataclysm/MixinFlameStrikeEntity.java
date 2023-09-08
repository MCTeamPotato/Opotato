package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.entity.effect.Flame_Strike_Entity;
import L_Ender.cataclysm.init.ModEffect;
import L_Ender.cataclysm.init.ModEntities;
import com.teampotato.opotato.api.entity.LightestEntity;
import com.teampotato.opotato.api.entity.UnupdatableInWaterEntity;
import com.teampotato.opotato.config.mods.CataclysmExtraConfig;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Mixin(Flame_Strike_Entity.class)
public abstract class MixinFlameStrikeEntity extends Entity implements LightestEntity, UnupdatableInWaterEntity {
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;", shift = At.Shift.BEFORE), cancellable = true)
    private void onTick(@NotNull CallbackInfo ci) {
        ci.cancel();
    }

    /**
     * @author Kasualix
     * @reason cache radius to avoid the additional overhead in getEntityData.
     */
    @Overwrite(remap = false)
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
    private static ThreadLocalRandom random = null;

    @Unique
    private static final double NO_PARTICLE = 0D;


    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextGaussian()D", remap = false))
    private double onGetGaussian(Random instance) {
        if (this.getRadius() > 10) return NO_PARTICLE;
        if (random == null) random = ThreadLocalRandom.current();
        return random.nextGaussian();
    }

    /**
     * @author Kasualix
     * @reason impl cache
     */
    @Overwrite(remap = false)
    public boolean isSoul() {
        return this.isSoul;
    }

    @Shadow(remap = false) @Final private static EntityDataAccessor<Float> DATA_RADIUS;

    @Shadow private LivingEntity owner;

    @Shadow(remap = false) private UUID ownerUUID;

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

    @Inject(method = "setOwner", at = @At("HEAD"), remap = false)
    private void onSetOwner(@NotNull LivingEntity ownerIn, CallbackInfo ci) {
        ResourceLocation type = ownerIn.getType().getRegistryName();
        if (type == null) return;
        this.isIgnisTheOwner = type.equals(ModEntities.IGNIS.get().getRegistryName());
        this.isPlayerTheOwner = type.equals(EntityType.PLAYER.getRegistryName());
    }

    /**
     * @author Kasualix
     * @reason impl cache
     */
    @Overwrite(remap = false)
    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level instanceof ServerLevel) {
            Entity entity = ((ServerLevel)this.level).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                ResourceLocation type = entity.getType().getRegistryName();
                if (type != null) {
                    this.isIgnisTheOwner = type.equals(ModEntities.IGNIS.get().getRegistryName());
                    this.isPlayerTheOwner = type.equals(EntityType.PLAYER.getRegistryName());
                }
                this.owner = (LivingEntity)entity;
            }
        }

        return this.owner;
    }

    @Inject(method = "setSoul", at = @At("HEAD"), remap = false)
    private void onSetSoul(boolean Soul, CallbackInfo ci) {
        this.isSoul = Soul;
    }
}

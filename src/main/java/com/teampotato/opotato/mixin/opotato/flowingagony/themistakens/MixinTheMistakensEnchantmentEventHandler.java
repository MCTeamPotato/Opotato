package com.teampotato.opotato.mixin.opotato.flowingagony.themistakens;

import com.teampotato.opotato.config.mods.FlowingAgonyExtraConfig;
import love.marblegate.flowingagony.config.Configuration;
import love.marblegate.flowingagony.damagesource.CustomDamageSource;
import love.marblegate.flowingagony.eventhandler.enchantment.TheMistakensEnchantmentEventHandler;
import love.marblegate.flowingagony.network.Networking;
import love.marblegate.flowingagony.network.packet.RemoveEffectSyncToClientPacket;
import love.marblegate.flowingagony.registry.EffectRegistry;
import love.marblegate.flowingagony.registry.EnchantmentRegistry;
import love.marblegate.flowingagony.util.EffectUtil;
import love.marblegate.flowingagony.util.EnchantmentUtil;
import love.marblegate.flowingagony.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

@Mixin(TheMistakensEnchantmentEventHandler.class)
public abstract class MixinTheMistakensEnchantmentEventHandler {
    /**
     * @author Kasualix
     * @reason remove strange allocation and impl config
     */
    @Overwrite(remap = false)
    @SubscribeEvent
    public static void doShadowbornEnchantmentEvent_applyAndRemoveEffect(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level;
        if (event.phase == TickEvent.Phase.START && player instanceof ServerPlayer) {
            BlockPos playerPos = player.blockPosition();
            boolean hasShadowBorn = EnchantmentUtil.isPlayerItemEnchanted(player, EnchantmentRegistry.SHADOWBORN.get(), EquipmentSlot.HEAD, EnchantmentUtil.ItemEncCalOp.GENERAL) == 1;
            if (player.hasEffect(MobEffects.BLINDNESS) && level.getMaxLocalRawBrightness(playerPos) >= FlowingAgonyExtraConfig.shadowBornLightLevelOnImmuningToBlindness.get() && hasShadowBorn) {
                player.removeEffectNoUpdate(MobEffects.BLINDNESS);
                Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player), new RemoveEffectSyncToClientPacket(MobEffects.BLINDNESS));
            }

            if (level.getMaxLocalRawBrightness(playerPos) <= FlowingAgonyExtraConfig.shadowBornLightLevelOnNightVision.get() && hasShadowBorn && !player.hasEffect(MobEffects.NIGHT_VISION)) {
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, FlowingAgonyExtraConfig.shadowBornNightVisionDuration.get()));
            }
        }
    }

    @Inject(method = "doCorruptedKindredEnchantmentEvent", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/entity/living/LivingDamageEvent;setAmount(F)V", ordinal = 0, remap = false), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void compatWithApotheosis(LivingDamageEvent event, CallbackInfo ci, int enchantLvl, Random temp) {
        if (enchantLvl > 5) {
            event.setCanceled(true);
            ci.cancel();
        }
    }

    @ModifyConstant(method = "doCorruptedKindredEnchantmentEvent", remap = false, constant = @Constant(floatValue = 0.5F))
    private static float baseCommonDmgReduction(float constant) {
        return FlowingAgonyExtraConfig.commonUndeadDamageReductionPercent.get().floatValue();
    }

    @ModifyConstant(method = "doCorruptedKindredEnchantmentEvent", remap = false, constant = @Constant(floatValue = 0.1F, ordinal = 1))
    private static float baseRareDmgReduction(float constant) {
        return FlowingAgonyExtraConfig.rareUndeadDamageReductionPercent.get().floatValue();
    }

    @ModifyConstant(method = "doCorruptedKindredEnchantmentEvent", remap = false, constant = @Constant(floatValue = 0.05F))
    private static float baseWitherDmgReduction(float constant) {
        return FlowingAgonyExtraConfig.witherDamageReductionPercent.get().floatValue();
    }

    @Inject(method = "doCorruptedKindredEnchantmentEvent", at = @At(value = "INVOKE", target = "Llove/marblegate/flowingagony/util/EntityUtil;isCommonUndead(Lnet/minecraft/world/entity/LivingEntity;)Z", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void compatWithApotheosis2(LivingDamageEvent event, CallbackInfo ci, int enchantLvl, Random temp) {
        LivingEntity entity = event.getEntityLiving();
        if (entity.hasEffect(EffectRegistry.CURSE_OF_UNDEAD.get())) return;
        if (temp.nextInt(100) < EnchantmentRegistry.CORRUPTED_KINDRED.get().getMaxLevel() + 1 - enchantLvl) {
            entity.addEffect(new MobEffectInstance(EffectRegistry.CURSE_OF_UNDEAD.get(), Integer.MAX_VALUE));
        }
    }

    @ModifyConstant(method = "doCorruptedKindredEnchantmentEvent", remap = false, constant = @Constant(intValue = 144000))
    private static int realInfinity(int constant) {
        return Integer.MAX_VALUE;
    }

    @Redirect(method = "doLightburnFungalParasiticEnchantmentEvent_applyProtectionAndSpreadFungalEffect", at = @At(value = "INVOKE", target = "Llove/marblegate/flowingagony/util/EntityUtil;getTargetsExceptOneself(Lnet/minecraft/world/entity/player/Player;FFLjava/util/function/Predicate;)Ljava/util/List;"))
    private static List<LivingEntity> onSpread(Player center, float radius, float height, Predicate<LivingEntity> predicate) {
        return EntityUtil.getTargetsExceptOneself(center, FlowingAgonyExtraConfig.fungalSpreadingRadius.get().floatValue(), FlowingAgonyExtraConfig.fungalSpreadingHeight.get().floatValue(), Configuration.GeneralSetting.VILLAGER_SAFE_MODE.get() ? (livingEntity) -> !(livingEntity instanceof AbstractVillager) : (x) -> true);
    }

    @ModifyConstant(method = "doLightburnFungalParasiticEnchantmentEvent_applyProtectionAndSpreadFungalEffect", remap = false, constant = @Constant(doubleValue = 0.125))
    private static double onCheckInfection(double constant) {
        return FlowingAgonyExtraConfig.maxSuccessfulInfectionProbability.get() / (EnchantmentRegistry.LIGHTBURN_FUNGAL_PARASITIC.get().getMaxLevel() + 1);
    }

    @ModifyConstant(method = "doLightburnFungalParasiticEnchantmentEvent_applyProtectionAndSpreadFungalEffect", remap = false, constant = @Constant(intValue = 120))
    private static int onInfect(int constant) {
        return FlowingAgonyExtraConfig.fungalInfectionEffectDuration.get().intValue();
    }

    @Inject(method = "doLightburnFungalParasiticEnchantmentEvent_applyProtectionAndSpreadFungalEffect", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/entity/living/LivingDamageEvent;isCanceled()Z", remap = false, shift = At.Shift.BEFORE), cancellable = true)
    private static void implConfig(LivingDamageEvent event, CallbackInfo ci) {
        ci.cancel();
        if (event.isCanceled()) return;
        int enchantmentLvl = EnchantmentUtil.isPlayerItemEnchanted((Player)event.getEntityLiving(), EnchantmentRegistry.LIGHTBURN_FUNGAL_PARASITIC.get(), EquipmentSlot.CHEST, EnchantmentUtil.ItemEncCalOp.TOTAL_LEVEL);
        DamageSource damageSource = event.getSource();
        if (damageSource.msgId.equals("fall")) {
            event.setAmount(event.getAmount() * (1.0F - (FlowingAgonyExtraConfig.maxFallDamageReduction.get().floatValue() / (EnchantmentRegistry.LIGHTBURN_FUNGAL_PARASITIC.get().getMaxLevel() + 1)) * (float) (enchantmentLvl + 1)));
        } else if (damageSource.isExplosion()) {
            event.setAmount(event.getAmount() * (1.0F - (FlowingAgonyExtraConfig.maxExplosionDamageReduction.get().floatValue() / (EnchantmentRegistry.LIGHTBURN_FUNGAL_PARASITIC.get().getMaxLevel() + 1)) * (float) (enchantmentLvl + 1)));
        } else if (damageSource.isFire()) {
            event.setAmount(event.getAmount() * (1.0F - (FlowingAgonyExtraConfig.maxFireDamageReduction.get().floatValue() / (EnchantmentRegistry.LIGHTBURN_FUNGAL_PARASITIC.get().getMaxLevel() + 1)) * (float) (enchantmentLvl + 1)));
        }
    }

    /**
     * @author Kasualix
     * @reason The {@link net.minecraft.world.level.Level#getEntitiesOfClass(Class, AABB)} method is very slow, tbh.
     */
    @Overwrite(remap = false)
    @SubscribeEvent
    public static void doBurialObjectCurseEvent(LivingDeathEvent event) {
        LivingEntity entity = event.getEntityLiving();
        Level level = entity.level;
        if (level instanceof ServerLevel && entity instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer) entity;
            if (EnchantmentUtil.isPlayerArmorEnchanted(player, EnchantmentRegistry.BURIAL_OBJECT.get(), EnchantmentUtil.ArmorEncCalOp.GENERAL) == 1) {
                for (Player anotherPlayer : level.players()) {
                    if (anotherPlayer.getStringUUID().equals(player.getStringUUID()) || !anotherPlayer.isAlive()) continue;
                    if (player.getBoundingBox().inflate(FlowingAgonyExtraConfig.burialObjectDetectionRadius.get(), FlowingAgonyExtraConfig.burialObjectDetectionHeight.get(), FlowingAgonyExtraConfig.burialObjectDetectionRadius.get()).contains(anotherPlayer.position())) {
                        anotherPlayer.hurt(CustomDamageSource.causeBurialObjectDamage(player), Float.MAX_VALUE);
                    }
                }
            }
        }
    }

    @ModifyConstant(method = "doOriginalSinErosionEnchantmentEvent_decreaseAttack", remap = false, constant = @Constant(floatValue = 5.0F))
    private static float onReduce(float constant) {
        return FlowingAgonyExtraConfig.originalSinErosionMinDamageReduction.get().floatValue() + EnchantmentRegistry.ORIGINAL_SIN_EROSION.get().getMaxLevel();
    }

    @ModifyConstant(method = "doOriginalSinErosionEnchantmentEvent_extraEXP", remap = false, constant = @Constant(doubleValue = 0.05, ordinal = 0))
    private static double setBaseBonus(double constant) {
        return FlowingAgonyExtraConfig.originalSinErosionBaseBonus.get();
    }

    @ModifyConstant(method = "doOriginalSinErosionEnchantmentEvent_extraEXP", remap = false, constant = @Constant(doubleValue = 0.05, ordinal = 1))
    private static double setBonusInterval(double constant) {
        return FlowingAgonyExtraConfig.originalSinErosionBonusInterval.get();
    }

    @ModifyConstant(method = "doPrototypeChaoticEnchantmentEvent", remap = false, constant = @Constant(intValue = 29))
    private static int onSetMaxBonus(int constant) {
        return FlowingAgonyExtraConfig.prototypeChaoticMaxHealthBonus.get();
    }

    @ModifyConstant(method = "doPrototypeChaoticEnchantmentEvent", remap = false, constant = @Constant(intValue = 1200))
    private static int onSetDuration(int constant) {
        return FlowingAgonyExtraConfig.prototypeChaoticMaxHealthBonusDuration.get();
    }

    /**
     * @author Kasualix
     * @reason avoid stream, impl config
     */
    @Overwrite(remap = false)
    @SubscribeEvent
    public static void doPrototypeChaoticTypeBetaEnchantmentEvent(PotionEvent.PotionAddedEvent event) {
        LivingEntity entity = event.getEntityLiving();
        Level level = entity.level;
        MobEffectInstance mobEffectInstance = event.getPotionEffect();
        if (!level.isClientSide() && entity instanceof Player && EnchantmentUtil.isPlayerItemEnchanted((Player)entity, EnchantmentRegistry.PROTOTYPE_CHAOTIC_TYPE_BETA.get(), EquipmentSlot.CHEST, EnchantmentUtil.ItemEncCalOp.GENERAL) == 1 && EffectUtil.isEffectShown(mobEffectInstance) && mobEffectInstance.getEffect().getCategory() == MobEffectCategory.BENEFICIAL && !mobEffectInstance.getEffect().isInstantenous()) {
            if (EnchantmentUtil.isPlayerItemEnchanted((Player)event.getEntityLiving(), EnchantmentRegistry.PROTOTYPE_CHAOTIC.get(), EquipmentSlot.CHEST, EnchantmentUtil.ItemEncCalOp.GENERAL) == 1) {
                mobEffectInstance.update(new MobEffectInstance(mobEffectInstance.getEffect(), mobEffectInstance.getDuration() * (1 + FlowingAgonyExtraConfig.prototypeChaoticTypeBetaWithProtoTypeChaoticExtraDurationBonus.get().intValue())));
                for (MobEffectInstance effectInstance : entity.getActiveEffects()) {
                    MobEffect effect = effectInstance.getEffect();
                    if (effect.getCategory() == MobEffectCategory.HARMFUL && !effectInstance.isCurativeItem(Items.MILK_BUCKET.getDefaultInstance()) && EffectUtil.isEffectShown(effectInstance)) {
                        event.getEntityLiving().removeEffect(effect);
                        Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntityLiving()), new RemoveEffectSyncToClientPacket(effect));
                    }
                }
            } else {
                mobEffectInstance.update(new MobEffectInstance(mobEffectInstance.getEffect(), mobEffectInstance.getDuration() * (1 + FlowingAgonyExtraConfig.prototypeChaoticTypeBetaExtraDurationBonus.get().intValue())));
            }
        }
    }

    @ModifyConstant(method = "doScholarOfOriginalSinEnchantmentEvent_addWeakness", remap = false, constant = @Constant(floatValue = 1.1F))
    private static float onIncreaseDamage(float constant) {
        return FlowingAgonyExtraConfig.scholarOfOriginalSinMinPlayerHurtBonusPercentage.get().floatValue() + ((float) EnchantmentRegistry.SCHOLAR_OF_ORIGINAL_SIN.get().getMaxLevel() * FlowingAgonyExtraConfig.scholarOfOriginalSinPlayerHurtBonusPerLevel.get().floatValue());
    }

    @ModifyConstant(method = "doScholarOfOriginalSinEnchantmentEvent_addWeakness", remap = false, constant = @Constant(floatValue = 0.1F))
    private static float onReduceDamageBasedOnLevel(float constant) {
        return FlowingAgonyExtraConfig.scholarOfOriginalSinPlayerHurtBonusPerLevel.get().floatValue();
    }

    @ModifyConstant(method = "doScholarOfOriginalSinEnchantmentEvent_addWeakness", remap = false, constant = @Constant(floatValue = 10.0F))
    private static float onGetMaxDamage(float constant) {
        return FlowingAgonyExtraConfig.scholarOfOriginalSinMaxPlayerHurtBonus.get().floatValue();
    }

    @ModifyConstant(method = "doScholarOfOriginalSinEnchantmentEvent_extendHarmfulEffect", remap = false, constant = @Constant(doubleValue = 2.1))
    private static double getMaxDurationBonus(double constant) {
        return (1.00 + FlowingAgonyExtraConfig.scholarOfOriginalSinMinNegativeEffectDurationBonus.get() + (double) EnchantmentRegistry.SCHOLAR_OF_ORIGINAL_SIN.get().getMaxLevel() * FlowingAgonyExtraConfig.scholarOfOriginalSinNegativeEffectDurationBonusPerLevel.get());
    }

    @ModifyConstant(method = "doScholarOfOriginalSinEnchantmentEvent_extendHarmfulEffect", remap = false, constant = @Constant(doubleValue = 0.1))
    private static double getDurationBonusInterval(double constant) {
        return FlowingAgonyExtraConfig.scholarOfOriginalSinNegativeEffectDurationBonusPerLevel.get();
    }

    @ModifyConstant(method = "doScholarOfOriginalSinEnchantmentEvent_extraEXP", remap = false, constant = @Constant(doubleValue = 0.35))
    private static double getBasicExtraExp(double constant) {
        return FlowingAgonyExtraConfig.scholarOfOriginalSinMaxExtraExp.get() - (double)EnchantmentRegistry.SCHOLAR_OF_ORIGINAL_SIN.get().getMaxLevel() * FlowingAgonyExtraConfig.scholarOfOriginalSinExtraExpBonusPerLevel.get();
    }

    @ModifyConstant(method = "doScholarOfOriginalSinEnchantmentEvent_extraEXP", remap = false, constant = @Constant(doubleValue = 0.15))
    private static double getBonusIntervalExp(double constant) {
        return FlowingAgonyExtraConfig.scholarOfOriginalSinExtraExpBonusPerLevel.get();
    }
}
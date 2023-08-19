package com.teampotato.opotato.mixin.opotato.headshot;

import chronosacaria.headshot.config.HeadshotConfig;
import chronosacaria.headshot.events.DealHeadshotEvent;
import com.teampotato.opotato.api.ExtendedLivingEntity;
import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = DealHeadshotEvent.class, remap = false)
public abstract class DealHeadshotEventMixin {
    /**
     * @author Kasualix
     * @reason fix crash
     */
    @Overwrite
    @SubscribeEvent
    public static void onHeadshotIfApplicable(LivingDamageEvent event) {
        if (event.getSource().getSourcePosition() == null) return;
        boolean ignore = false;
        if (event.getSource().isProjectile()) {
            Entity trueSource = event.getSource().getEntity();
            LivingEntity entity = event.getEntityLiving();
            double headStart = entity.position().add(0.0, (double)entity.getDimensions(entity.getPose()).height * 0.85, 0.0).y - 0.17;
            if (!ignore && doesNotHaveHelmet(event.getEntity()) && event.getSource().getSourcePosition().y > headStart && event.getSource() != null && !((ExtendedLivingEntity)entity).opotato$isDamageSourceBlocked(event.getSource())) {
                if (event.getEntity() instanceof AnimalEntity || event.getEntity() instanceof WaterMobEntity || event.getEntity() instanceof SlimeEntity || event.getEntity() instanceof EnderDragonEntity) {
                    return;
                }

                if (event.getSource().getSourcePosition().y > headStart && entity instanceof ServerPlayerEntity) {
                    ((ServerPlayerEntity)entity).displayClientMessage(new TranslationTextComponent("headshot.opotato.on_player"), true);
                    if (PotatoCommonConfig.ENABLE_HEADSHOT_SOUND_DING.get() && event.getEntityLiving() instanceof PlayerEntity) event.getEntityLiving().playSound(SoundEvents.ARROW_HIT_PLAYER, PotatoCommonConfig.HEADSHOT_VOLUME.get(), PotatoCommonConfig.HEADSHOT_PITCH.get());
                }

                if (event.getSource().getEntity() instanceof ServerPlayerEntity && trueSource != null) {
                    ((ServerPlayerEntity)trueSource).displayClientMessage(new TranslationTextComponent("headshot.opotato.on_entity"), true);
                    if (PotatoCommonConfig.ENABLE_HEADSHOT_SOUND_DING.get() && event.getEntityLiving() instanceof PlayerEntity) event.getEntityLiving().playSound(SoundEvents.ARROW_HIT_PLAYER, PotatoCommonConfig.HEADSHOT_VOLUME.get(), PotatoCommonConfig.HEADSHOT_PITCH.get());
                }

                double headshotDamage = (double)event.getAmount() * (Double) HeadshotConfig.HEADSHOT_DAMAGE_MULTIPLIER.get();
                double projectileProtectionDamageReduction = (Double)HeadshotConfig.HEADSHOT_PROJECTILE_PROTECTION_DAMAGE_REDUCTION.get();
                if (hasProjectileProtection(event.getEntity())) {
                    event.setAmount((float)headshotDamage * (float)projectileProtectionDamageReduction);
                    event.getEntityLiving().getItemBySlot(EquipmentSlotType.HEAD).hurt((int)headshotDamage / 4, event.getEntity().getCommandSenderWorld().random, (ServerPlayerEntity)null);
                } else if (!hasProjectileProtection(event.getEntity())) {
                    event.setAmount((float)headshotDamage);
                    event.getEntityLiving().getItemBySlot(EquipmentSlotType.HEAD).hurt((int)headshotDamage / 2, event.getEntity().getCommandSenderWorld().random, (ServerPlayerEntity)null);
                    ignore = true;
                    if ((Boolean)HeadshotConfig.DO_BLINDNESS.get()) {
                        entity.addEffect(new EffectInstance(Effects.BLINDNESS, (Integer)HeadshotConfig.BLIND_TICKS.get(), 3));
                    }

                    if ((Boolean)HeadshotConfig.DO_NAUSEA.get()) {
                        entity.addEffect(new EffectInstance(Effects.CONFUSION, (Integer)HeadshotConfig.NAUSEA_TICKS.get(), 2));
                    }

                    return;
                }

                ignore = false;
            }
        }

    }

    @Shadow
    private static boolean doesNotHaveHelmet(Entity entity) {
        return (Boolean)HeadshotConfig.HELMET_MITIGATION.get() ? ((LivingEntity)entity).getItemBySlot(EquipmentSlotType.HEAD).isEmpty() : entity instanceof LivingEntity;
    }

    @Shadow
    private static boolean hasProjectileProtection(Entity entity) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION, (LivingEntity)entity) > 0;
    }
}

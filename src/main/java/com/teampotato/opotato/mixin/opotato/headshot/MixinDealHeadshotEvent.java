package com.teampotato.opotato.mixin.opotato.headshot;

import chronosacaria.headshot.config.HeadshotConfig;
import chronosacaria.headshot.events.DealHeadshotEvent;
import com.teampotato.opotato.api.ILivingEntity;
import com.teampotato.opotato.config.mods.HeadshotExtraConfig;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Slime;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = DealHeadshotEvent.class, remap = false)
public abstract class MixinDealHeadshotEvent {

    @Shadow
    private static boolean hasProjectileProtection(Entity entity) {
        throw new RuntimeException();
    }

    @Shadow
    private static boolean doesNotHaveHelmet(Entity entity) {
        throw new RuntimeException();
    }

    /**
     * @author Kasualix
     * @reason impl fix and localization
     */
    @Overwrite
    @SubscribeEvent
    public static void onHeadshotIfApplicable(LivingDamageEvent event) {
        if (event.getSource().isProjectile()) {
            Entity trueSource = event.getSource().getEntity();
            LivingEntity entity = event.getEntityLiving();
            double headStart = entity.position().add(0.0, (double)entity.getDimensions(entity.getPose()).height * 0.85, 0.0).y - 0.17;
            if (doesNotHaveHelmet(event.getEntity()) && event.getSource().getSourcePosition().y > headStart && event.getSource() != null && !((ILivingEntity) entity).potato$isDamageSourceBlocked(event.getSource())) {
                if (event.getEntity() instanceof Animal || event.getEntity() instanceof WaterAnimal || event.getEntity() instanceof Slime || event.getEntity() instanceof EnderDragon) {
                    return;
                }

                if (event.getSource().getSourcePosition().y > headStart && entity instanceof ServerPlayer) {
                    if (HeadshotExtraConfig.enableDingOnHeadshot.get()) entity.playSound(SoundEvents.ARROW_HIT_PLAYER, HeadshotExtraConfig.dingOnHeadshotVolume.get().floatValue(), HeadshotExtraConfig.dingOnHeadshotPitch.get().floatValue());
                    ((ServerPlayer)entity).displayClientMessage(new TranslatableComponent("headshot.opotato.on_player"), true);
                }

                if (event.getSource().getEntity() instanceof ServerPlayer && trueSource != null) {
                    if (HeadshotExtraConfig.enableDingOnHeadshot.get()) trueSource.playSound(SoundEvents.ARROW_HIT_PLAYER, HeadshotExtraConfig.dingOnHeadshotVolume.get().floatValue(), HeadshotExtraConfig.dingOnHeadshotPitch.get().floatValue());
                    ((ServerPlayer)trueSource).displayClientMessage(new TranslatableComponent("headshot.opotato.on_entity"), true);
                }

                double headshotDamage = (double)event.getAmount() * HeadshotConfig.HEADSHOT_DAMAGE_MULTIPLIER.get();
                double projectileProtectionDamageReduction = HeadshotConfig.HEADSHOT_PROJECTILE_PROTECTION_DAMAGE_REDUCTION.get();
                if (hasProjectileProtection(event.getEntity())) {
                    event.setAmount((float)headshotDamage * (float)projectileProtectionDamageReduction);
                    event.getEntityLiving().getItemBySlot(EquipmentSlot.HEAD).hurt((int)headshotDamage / 4, event.getEntity().getCommandSenderWorld().random, null);
                } else {
                    event.setAmount((float)headshotDamage);
                    event.getEntityLiving().getItemBySlot(EquipmentSlot.HEAD).hurt((int)headshotDamage / 2, event.getEntity().getCommandSenderWorld().random, null);
                    if (HeadshotConfig.DO_BLINDNESS.get()) {
                        entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, HeadshotConfig.BLIND_TICKS.get(), 3));
                    }

                    if (HeadshotConfig.DO_NAUSEA.get()) {
                        entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, HeadshotConfig.NAUSEA_TICKS.get(), 2));
                    }

                    return;
                }
            }
        }
    }
}

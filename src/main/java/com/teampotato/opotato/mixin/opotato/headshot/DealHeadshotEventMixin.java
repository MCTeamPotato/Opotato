package com.teampotato.opotato.mixin.opotato.headshot;

import chronosacaria.headshot.config.HeadshotConfig;
import chronosacaria.headshot.events.DealHeadshotEvent;
import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = DealHeadshotEvent.class, remap = false)
public class DealHeadshotEventMixin {

    @Shadow
    private static boolean doesNotHaveHelmet(Entity entity) {
        return HeadshotConfig.HELMET_MITIGATION.get() ? ((LivingEntity)entity).getItemBySlot(EquipmentSlotType.HEAD).isEmpty() : entity instanceof LivingEntity;
    }

    @Shadow
    private static boolean hasProjectileProtection(Entity entity) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION, (LivingEntity)entity) > 0;
    }

    /**
     * @author Doctor Who
     * @reason Saving The World
     */
    @Overwrite
    @SubscribeEvent
    public static void onHeadshotIfApplicable(LivingDamageEvent event) {
        DamageSource eventSource = event.getSource();
        Vector3d srcPos = eventSource.getSourcePosition();
        Entity trueSource = eventSource.getEntity();
        LivingEntity entity = event.getEntityLiving();
        double headStart = entity.position().add(0.0, entity.getDimensionsForge(entity.getPose()).height * 0.85, 0.0).y - 0.17;
        if (!eventSource.isProjectile() || srcPos == null || entity.isInvulnerableTo(eventSource) || srcPos.y <= headStart || !doesNotHaveHelmet(entity)) return;

        ServerPlayerEntity serverPlayerEntity = (entity instanceof ServerPlayerEntity) ? (ServerPlayerEntity) entity : null;
        if (serverPlayerEntity != null) serverPlayerEntity.displayClientMessage(new TranslationTextComponent("headshot.opotato.on_player"), true);

        ServerPlayerEntity serverEntity = (trueSource instanceof ServerPlayerEntity) ? (ServerPlayerEntity) trueSource : null;
        if (serverEntity != null) serverEntity.displayClientMessage(new TranslationTextComponent("headshot.opotato.on_entity"), true);

        double headshotDamage = event.getAmount() * HeadshotConfig.HEADSHOT_DAMAGE_MULTIPLIER.get();

        if (hasProjectileProtection(entity)) {
            event.setAmount((float) (headshotDamage * HeadshotConfig.HEADSHOT_PROJECTILE_PROTECTION_DAMAGE_REDUCTION.get()));
            entity.getItemBySlot(EquipmentSlotType.HEAD).hurt((int) (headshotDamage / 4), entity.getCommandSenderWorld().random, null);
        } else {
            event.setAmount((float) headshotDamage);
            entity.getItemBySlot(EquipmentSlotType.HEAD).hurt((int) (headshotDamage / 2), entity.getCommandSenderWorld().random, null);

            if (HeadshotConfig.DO_BLINDNESS.get()) entity.addEffect(new EffectInstance(Effects.BLINDNESS, HeadshotConfig.BLIND_TICKS.get(), 3));
            if (HeadshotConfig.DO_NAUSEA.get()) entity.addEffect(new EffectInstance(Effects.CONFUSION, HeadshotConfig.NAUSEA_TICKS.get(), 2));
            if (PotatoCommonConfig.ENABLE_HEADSHOT_SOUND_DING.get() && entity instanceof ServerPlayerEntity)
                entity.playSound(SoundEvents.ARROW_HIT_PLAYER, PotatoCommonConfig.HEADSHOT_VOLUME.get(), PotatoCommonConfig.HEADSHOT_PITCH.get());
        }
    }
}

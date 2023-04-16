package com.teampotato.opotato.mixin.opotato.headshot;

import chronosacaria.headshot.config.HeadshotConfig;
import chronosacaria.headshot.events.DealHeadshotEvent;
import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = DealHeadshotEvent.class, remap = false)
public class DealHeadshotEventMixin {
    /**
     * @author Kasualix
     * @reason Optimize someone's shit
     */
    @Overwrite
    @SubscribeEvent
    public static void onHeadshotIfApplicable(LivingDamageEvent event) {
        LivingEntity entity = event.getEntityLiving();
        World world = entity.level;
        DamageSource eventSource = event.getSource();
        Vector3d srcPos = eventSource.getSourcePosition();
        if (world.isClientSide || !eventSource.isProjectile() || srcPos == null || entity.isInvulnerableTo(eventSource) || srcPos.y <= entity.position().add(0.0, entity.getDimensionsForge(entity.getPose()).height * 0.85, 0.0).y - 0.17 || !(!HeadshotConfig.HELMET_MITIGATION.get() || entity.getItemBySlot(EquipmentSlotType.HEAD).isEmpty())) return;

        PlayerEntity player = (entity instanceof PlayerEntity) ? (PlayerEntity) entity : null;
        if (player != null) player.displayClientMessage(new TranslationTextComponent("headshot.opotato.on_player"), true);

        Entity trueSource = eventSource.getEntity();
        PlayerEntity sourcePlayer = (trueSource instanceof PlayerEntity) ? (PlayerEntity) trueSource : null;
        if (sourcePlayer != null) sourcePlayer.displayClientMessage(new TranslationTextComponent("headshot.opotato.on_entity"), true);

        double headshotDamage = event.getAmount() * HeadshotConfig.HEADSHOT_DAMAGE_MULTIPLIER.get();
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION, entity) > 0) {
            event.setAmount((float) (headshotDamage * HeadshotConfig.HEADSHOT_PROJECTILE_PROTECTION_DAMAGE_REDUCTION.get()));
            entity.getItemBySlot(EquipmentSlotType.HEAD).hurt((int) (headshotDamage / 4), world.random, null);
        } else {
            event.setAmount((float) headshotDamage);
            entity.getItemBySlot(EquipmentSlotType.HEAD).hurt((int) (headshotDamage / 2), world.random, null);
            if (HeadshotConfig.DO_BLINDNESS.get()) entity.addEffect(new EffectInstance(Effects.BLINDNESS, HeadshotConfig.BLIND_TICKS.get(), 3));
            if (HeadshotConfig.DO_NAUSEA.get()) entity.addEffect(new EffectInstance(Effects.CONFUSION, HeadshotConfig.NAUSEA_TICKS.get(), 2));
        }
        if (PotatoCommonConfig.ENABLE_HEADSHOT_SOUND_DING.get() && entity instanceof PlayerEntity) entity.playSound(SoundEvents.ARROW_HIT_PLAYER, PotatoCommonConfig.HEADSHOT_VOLUME.get(), PotatoCommonConfig.HEADSHOT_PITCH.get());
    }
}

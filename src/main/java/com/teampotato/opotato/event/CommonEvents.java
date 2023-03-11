package com.teampotato.opotato.event;

import com.teampotato.opotato.config.PotatoCommonConfig;
import com.teampotato.opotato.util.HeadshotUtils;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommonEvents {
    @SubscribeEvent
    public static void onHeadshot(LivingHurtEvent event) {
        if (!event.getSource().isProjectile() || !PotatoCommonConfig.ENABLE_HEADSHOT.get()) return;
        LivingEntity attacked = event.getEntityLiving();
        if (!HeadshotUtils.calculateIsHeadHit(event.getSource().getSourcePosition(), attacked) || attacked.isInvulnerableTo(event.getSource())) return;
        if (attacked instanceof Player) {
            ((Player) attacked).displayClientMessage(new TranslatableComponent("headshot.opotato.on_player"), true);
        } else {
            Entity archer = event.getSource().getDirectEntity();
            if (archer == null) return;
            if (archer instanceof Player) {
                ((Player) archer).displayClientMessage(new TranslatableComponent("headshot.opotato.on_entity"), true);
            }
        }
        if (PotatoCommonConfig.PLAY_HEADSHOT_SOUND.get()) attacked.playSound(SoundEvents.ARROW_HIT_PLAYER, 1, 1);
        event.setAmount((float) (event.getAmount() * PotatoCommonConfig.DAMAGE_MULTIPLIER.get()));
        if (PotatoCommonConfig.DO_BLIND.get()) attacked.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, PotatoCommonConfig.BLIND_TICKS.get(), 3));
        if (PotatoCommonConfig.DO_NAUSEA.get()) attacked.addEffect(new MobEffectInstance(MobEffects.CONFUSION, PotatoCommonConfig.NAUSEA_TICKS.get(), 2));
    }
}

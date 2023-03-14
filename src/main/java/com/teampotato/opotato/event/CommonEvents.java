package com.teampotato.opotato.event;

import com.teampotato.opotato.OpotatoCommand;
import com.teampotato.opotato.config.PotatoCommonConfig;
import com.teampotato.opotato.util.HeadshotUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
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

        if (attacked instanceof PlayerEntity) {
            ((PlayerEntity) attacked).displayClientMessage(new TranslationTextComponent("headshot.opotato.on_player"), true);
        } else {
            Entity archer = event.getSource().getDirectEntity();
            if (archer instanceof PlayerEntity) ((PlayerEntity) archer).displayClientMessage(new TranslationTextComponent("headshot.opotato.on_entity"), true);
        }

        if (PotatoCommonConfig.PLAY_HEADSHOT_SOUND.get()) attacked.playSound(SoundEvents.ARROW_HIT_PLAYER, 1, 1);
        event.setAmount((float) (event.getAmount() * PotatoCommonConfig.DAMAGE_MULTIPLIER.get()));

        if (PotatoCommonConfig.DO_BLIND.get())
            attacked.addEffect(new EffectInstance(Effects.BLINDNESS, PotatoCommonConfig.BLIND_TICKS.get(), 3));
        if (PotatoCommonConfig.DO_NAUSEA.get())
            attacked.addEffect(new EffectInstance(Effects.CONFUSION, PotatoCommonConfig.NAUSEA_TICKS.get(), 2));
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        OpotatoCommand.register(event.getDispatcher());
    }
}

package com.teampotato.opotato.mixin.opotato.headshot;

import chronosacaria.headshot.events.DealHeadshotEvent;
import com.teampotato.opotato.config.HeadshotExtraConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DealHeadshotEvent.class, remap = false)
public abstract class MixinDealHeadshotEvent {
    @Unique
    private static final ThreadLocal<LivingEntity> potato$hurtEntity = new ThreadLocal<>();

    @Inject(method = "onHeadshotIfApplicable", at = @At("HEAD"), cancellable = true)
    private static void fixNPE(LivingDamageEvent event, CallbackInfo ci) {
        if (event.getSource().getSourcePosition() == null) {
            ci.cancel();
        } else {
            potato$hurtEntity.set(event.getEntityLiving());
        }
    }

    @Redirect(method = "onHeadshotIfApplicable", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;displayClientMessage(Lnet/minecraft/network/chat/Component;Z)V", ordinal = 0))
    private static void localizeOnPlayer(ServerPlayer instance, Component arg, boolean bl) {
        instance.displayClientMessage(new TranslatableComponent("headshot.opotato.on_player"), true);
        if (HeadshotExtraConfig.enableDingOnHeadshot.get()) potato$hurtEntity.get().playSound(SoundEvents.ARROW_HIT_PLAYER, HeadshotExtraConfig.dingOnHeadshotVolume.get().floatValue(), HeadshotExtraConfig.dingOnHeadshotPitch.get().floatValue());
    }

    @Redirect(method = "onHeadshotIfApplicable", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;displayClientMessage(Lnet/minecraft/network/chat/Component;Z)V", ordinal = 1))
    private static void localizeOnEntity(ServerPlayer instance, Component arg, boolean bl) {
        instance.displayClientMessage(new TranslatableComponent("headshot.opotato.on_entity"), true);
        if (HeadshotExtraConfig.enableDingOnHeadshot.get()) potato$hurtEntity.get().playSound(SoundEvents.ARROW_HIT_PLAYER, HeadshotExtraConfig.dingOnHeadshotVolume.get().floatValue(), HeadshotExtraConfig.dingOnHeadshotPitch.get().floatValue());
    }
}

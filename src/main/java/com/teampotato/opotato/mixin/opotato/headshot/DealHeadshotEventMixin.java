package com.teampotato.opotato.mixin.opotato.headshot;

import chronosacaria.headshot.events.DealHeadshotEvent;
import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DealHeadshotEvent.class, remap = false)
public abstract class DealHeadshotEventMixin {
    @Inject(method = "onHeadshotIfApplicable", at = @At("HEAD"), cancellable = true)
    private static void onHeadshot(LivingDamageEvent event, CallbackInfo ci) {
        if (event.getSource().getSourcePosition() == null) ci.cancel();
    }
    
    @Redirect(method = "onHeadshotIfApplicable", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ServerPlayerEntity;displayClientMessage(Lnet/minecraft/util/text/ITextComponent;Z)V", ordinal = 0))
    private static void onDisplayMessage1(ServerPlayerEntity instance, ITextComponent pChatComponent, boolean pActionBar) {
        instance.displayClientMessage(new TranslationTextComponent("headshot.opotato.on_player"), true);
        if (PotatoCommonConfig.ENABLE_HEADSHOT_SOUND_DING.get())
            event.getEntityLiving().playSound(SoundEvents.ARROW_HIT_PLAYER, PotatoCommonConfig.HEADSHOT_VOLUME.get(), PotatoCommonConfig.HEADSHOT_PITCH.get());
    }

    @Redirect(method = "onHeadshotIfApplicable", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ServerPlayerEntity;displayClientMessage(Lnet/minecraft/util/text/ITextComponent;Z)V", ordinal = 1))
    private static void onDisplayMessage2(ServerPlayerEntity instance, ITextComponent pChatComponent, boolean pActionBar) {
        instance.displayClientMessage(new TranslationTextComponent("headshot.opotato.on_entity"), true);
        if (PotatoCommonConfig.ENABLE_HEADSHOT_SOUND_DING.get())
            event.getEntityLiving().playSound(SoundEvents.ARROW_HIT_PLAYER, PotatoCommonConfig.HEADSHOT_VOLUME.get(), PotatoCommonConfig.HEADSHOT_PITCH.get());
    }
}

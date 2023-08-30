package com.teampotato.opotato.mixin.opotato.cataclysm.rubidium.extra;

import L_Ender.cataclysm.items.The_Incinerator;
import com.teampotato.opotato.Opotato;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(The_Incinerator.class)
public abstract class MixinTheIncinerator {
    @Inject(method = "onUsingTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", shift = At.Shift.AFTER))
    private void fogHint(ItemStack stack, LivingEntity player, int count, CallbackInfo ci) {
        if (player instanceof LocalPlayer && !player.getTags().contains(Opotato.MOD_ID + ".sodiumExtra.fogHint")) {
            ((Player)player).displayClientMessage(new TranslatableComponent("opotato.catatclysm.incinerator.fogHint"), false);
            player.addTag(Opotato.MOD_ID + ".sodiumExtra.fogHint");
        }
    }
}

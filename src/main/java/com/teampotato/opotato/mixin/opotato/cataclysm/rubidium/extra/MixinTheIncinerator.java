package com.teampotato.opotato.mixin.opotato.cataclysm.rubidium.extra;

import L_Ender.cataclysm.items.The_Incinerator;
import com.teampotato.opotato.EarlySetupInitializer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(The_Incinerator.class)
public abstract class MixinTheIncinerator {
    @Inject(method = "onUsingTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", shift = At.Shift.AFTER))
    private void fogHint(ItemStack stack, @NotNull LivingEntity livingEntity, int count, CallbackInfo ci) {
        ServerPlayer serverPlayer = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(livingEntity.getUUID());
        if (livingEntity instanceof LocalPlayer && serverPlayer != null && !serverPlayer.getTags().contains(EarlySetupInitializer.MOD_ID + ".sodiumExtra.fogHint")) {
            ((LocalPlayer)livingEntity).displayClientMessage(new TranslatableComponent("opotato.catatclysm.incinerator.fogHint"), false);
            serverPlayer.addTag(EarlySetupInitializer.MOD_ID + ".sodiumExtra.fogHint");
        }
    }
}

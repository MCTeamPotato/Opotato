package com.teampotato.opotato.mixin.keybind;

import com.mojang.authlib.GameProfile;
import com.teampotato.opotato.event.CommonEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(LocalPlayer.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayer {
    @Shadow public abstract void sendMessage(Component p_108693_, UUID p_108694_);

    public MixinClientPlayerEntity(ClientLevel p_108548_, GameProfile p_108549_) {
        super(p_108548_, p_108549_);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (this.getTags().contains("opotato.one_punch")) {
            this.removeTag("opotato.one_punch");
            CommonEvents.creativeOnePunch = !CommonEvents.creativeOnePunch;
            this.sendMessage(Component.nullToEmpty("Creative One Punch: Currently " + CommonEvents.creativeOnePunch), this.uuid);
        }
    }
}

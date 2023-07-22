package com.teampotato.opotato.mixin.keybind;

import com.mojang.authlib.GameProfile;
import com.teampotato.opotato.event.PotatoEvents;
import com.teampotato.opotato.keybind.OnePunchKey;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.text.StringTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity {

    public MixinClientPlayerEntity(ClientWorld p_i50991_1_, GameProfile p_i50991_2_) {
        super(p_i50991_1_, p_i50991_2_);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (this.getTags().contains("opotato.one_punch")) {
            this.removeTag("opotato.one_punch");
            PotatoEvents.creativeOnePunch = !PotatoEvents.creativeOnePunch;
            this.sendMessage(new StringTextComponent("Creative One Punch: Currently " + PotatoEvents.creativeOnePunch), this.uuid);
        }
    }
}

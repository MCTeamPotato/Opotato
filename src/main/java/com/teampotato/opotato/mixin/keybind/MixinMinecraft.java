package com.teampotato.opotato.mixin.keybind;

import com.teampotato.opotato.keybind.OnePunchKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow @Nullable public LocalPlayer player;

    @Inject(method = "tick", at = @At("RETURN"))
    private void onTick(CallbackInfo ci) {
        if (OnePunchKey.switchOnePunchKey.consumeClick() && player != null) this.player.addTag("opotato.one_punch");
    }
}
package com.teampotato.opotato.mixin.keybind;

import com.teampotato.opotato.keybind.OnePunchKey;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(Options.class)
public abstract class MixinGameSettings {
    @Shadow public KeyMapping[] keyMappings;

    @Inject(method = "<init>", at = @At("RETURN"))
    private synchronized void onInit(Minecraft pMinecraft, File pGameDirectory, CallbackInfo ci) {
        this.keyMappings = ArrayUtils.add(keyMappings, OnePunchKey.switchOnePunchKey);
    }
}
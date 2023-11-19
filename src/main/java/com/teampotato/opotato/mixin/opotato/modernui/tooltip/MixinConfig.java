package com.teampotato.opotato.mixin.opotato.modernui.tooltip;

import icyllis.modernui.forge.Config;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Config.Client.class)
public abstract class MixinConfig {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraftforge/common/ForgeConfigSpec$Builder;define(Ljava/lang/String;Z)Lnet/minecraftforge/common/ForgeConfigSpec$BooleanValue;", remap = false))
    private ForgeConfigSpec.BooleanValue onEnableTooltip(ForgeConfigSpec.@NotNull Builder instance, String path, boolean defaultValue) {
        return instance.define(path, false);
    }
}

package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.infernal_forge;
import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.item.Item;
import net.minecraft.util.CooldownTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(infernal_forge.class)
public abstract class MixinInfernalForge {
    @Redirect(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/CooldownTracker;addCooldown(Lnet/minecraft/item/Item;I)V"))
    private void redirectUseOn(CooldownTracker instance, Item item, int ticks) {
        instance.addCooldown((infernal_forge)(Object)this, PotatoCommonConfig.INFERNAL_FORGE_COOL_DOWN.get());
    }
}

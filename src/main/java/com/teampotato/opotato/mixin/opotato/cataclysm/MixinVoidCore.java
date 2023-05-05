package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.void_core;
import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.item.Item;
import net.minecraft.util.CooldownTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(void_core.class)
public abstract class MixinVoidCore {
    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/CooldownTracker;addCooldown(Lnet/minecraft/item/Item;I)V"))
    private void onUse(CooldownTracker instance, Item item, int coolDown) {
        instance.addCooldown((void_core)(Object)this, PotatoCommonConfig.VOID_CORE_COOL_DOWN.get());
    }
}

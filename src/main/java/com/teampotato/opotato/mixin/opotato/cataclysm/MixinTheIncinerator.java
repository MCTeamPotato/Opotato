package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.The_Incinerator;
import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.item.Item;
import net.minecraft.util.CooldownTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(The_Incinerator.class)
public abstract class MixinTheIncinerator {
    @Redirect(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/CooldownTracker;addCooldown(Lnet/minecraft/item/Item;I)V"))
    private void onAddCoolDown(CooldownTracker instance, Item item, int time) {
        instance.addCooldown((The_Incinerator)(Object)this, PotatoCommonConfig.INCINERATOR_COOL_DOWN.get());
    }
}

package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.Bulwark_of_the_flame;
import com.teampotato.opotato.config.mods.CataclysmExtraJsonConfig;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bulwark_of_the_flame.class)
public abstract class MixinBulwarkOfTheFlame extends Item {
    public MixinBulwarkOfTheFlame(Properties arg) {
        super(arg);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(Properties group, CallbackInfo ci) {
        if (CataclysmExtraJsonConfig.bulwarkOfTheFlameDamageable) ((ItemAccessor)this).setMaxDamage(CataclysmExtraJsonConfig.bulwarkOfTheFlameDurability);
    }
}

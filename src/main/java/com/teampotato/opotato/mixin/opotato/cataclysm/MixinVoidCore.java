package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.void_core;
import com.teampotato.opotato.config.mods.CataclysmExtraConfig;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(void_core.class)
public abstract class MixinVoidCore extends Item {
    public MixinVoidCore(Properties arg) {
        super(arg);
    }
    @ModifyConstant(method = "use", constant = @Constant(intValue = 120))
    private int onSetCoolDown(int constant) {
        return CataclysmExtraConfig.voidCoreCoolDown.get();
    }
}

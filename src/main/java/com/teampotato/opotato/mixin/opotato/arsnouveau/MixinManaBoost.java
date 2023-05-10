package com.teampotato.opotato.mixin.opotato.arsnouveau;

import com.hollingsworth.arsnouveau.common.enchantment.ManaBoost;
import com.teampotato.opotato.config.PotatoCommonConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = ManaBoost.class, remap = false)
public abstract class MixinManaBoost {
    /**
     * @author Kasualix
     * @reason implement config
     */
    @Overwrite
    public int getMaxLevel() {
        return PotatoCommonConfig.MANA_BOOST_ENCHANTMENT_MAX_LEVEL.get();
    }
}

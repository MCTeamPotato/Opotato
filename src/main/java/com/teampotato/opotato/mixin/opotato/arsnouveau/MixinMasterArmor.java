package com.teampotato.opotato.mixin.opotato.arsnouveau;

import com.hollingsworth.arsnouveau.common.armor.MasterArmor;
import com.teampotato.opotato.config.PotatoCommonConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = MasterArmor.class, remap = false)
public abstract class MixinMasterArmor {
    /**
     * @author Kasualix
     * @reason implement config
     */
    @Overwrite
    public int getMaxManaBoost() {
        return PotatoCommonConfig.MASTER_ARMOR_MAX_MANA_BOOST.get();
    }

    /**
     * @author Kasualix
     * @reason implement config
     */
    @Overwrite
    public int getManaRegenBonus() {
        return PotatoCommonConfig.MASTER_ARMOR_MANA_REGEN_BONUS.get();
    }
}

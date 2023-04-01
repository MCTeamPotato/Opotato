package com.teampotato.opotato.mixin.opotato.blueskies;

import com.legacy.blue_skies.world.general_features.structures.GatekeeperHouseStructure;
import com.teampotato.opotato.config.PotatoCommonConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = GatekeeperHouseStructure.class, remap = false)
public class MixinGatekeeperHouseStructure {

    /**
     * @author Doctor Who
     * @reason Saving The World
     */
    @Overwrite
    public int getSpacing() {
        return PotatoCommonConfig.GATE_KEEPER_HOUSE_SPACING.get();
    }
}

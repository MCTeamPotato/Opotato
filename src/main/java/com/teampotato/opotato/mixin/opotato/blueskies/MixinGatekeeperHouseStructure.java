package com.teampotato.opotato.mixin.opotato.blueskies;

import com.legacy.blue_skies.world.general_features.structures.GatekeeperHouseStructure;
import com.teampotato.opotato.config.BlueSkiesExtraConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = GatekeeperHouseStructure.class, remap = false)
public abstract class MixinGatekeeperHouseStructure {
    /**
     * @author Kasualix
     * @reason impl config
     */
    @Overwrite
    public int getSpacing() {
        return BlueSkiesExtraConfig.gateKeeperHouseSpacing.get();
    }
}

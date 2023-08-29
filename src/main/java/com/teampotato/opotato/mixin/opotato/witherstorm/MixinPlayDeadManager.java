package com.teampotato.opotato.mixin.opotato.witherstorm;

import nonamecrackers2.witherstormmod.common.util.PlayDeadManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayDeadManager.class)
public abstract class MixinPlayDeadManager {
    @Unique
    private static final PlayDeadManager.State[] PLAY_DEAD_MANAGER_STATES = PlayDeadManager.State.values();

    @Redirect(method = "nextState", at = @At(value = "INVOKE", target = "Lnonamecrackers2/witherstormmod/common/util/PlayDeadManager$State;values()[Lnonamecrackers2/witherstormmod/common/util/PlayDeadManager$State;", remap = false), remap = false)
    private PlayDeadManager.State[] avoidAllocation() {
        return PLAY_DEAD_MANAGER_STATES;
    }
}

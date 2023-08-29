package com.teampotato.opotato.mixin.opotato.witherstorm;

import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CommandBlockEntity.class)
public abstract class MixinCommandBlockEntity {
    @Shadow(remap = false) public abstract CommandBlockEntity.State getState();

    @Shadow(remap = false) public abstract void setState(CommandBlockEntity.State state);

    @Unique
    private static final CommandBlockEntity.Mode[] ALL_MODES = CommandBlockEntity.Mode.values();
    @Unique
    private static final CommandBlockEntity.State[] ALL_STATES = CommandBlockEntity.State.values();

    @Unique
    private static final int states_length = ALL_STATES.length;

    @Redirect(method = "readAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnonamecrackers2/witherstormmod/common/entity/CommandBlockEntity$Mode;values()[Lnonamecrackers2/witherstormmod/common/entity/CommandBlockEntity$Mode;", remap = false))
    private CommandBlockEntity.Mode[] avoidModeValues() {
        return ALL_MODES;
    }

    @Redirect(method = "readAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnonamecrackers2/witherstormmod/common/entity/CommandBlockEntity$State;values()[Lnonamecrackers2/witherstormmod/common/entity/CommandBlockEntity$State;", remap = false))
    private CommandBlockEntity.State[] avoidStateValues() {
        return ALL_STATES;
    }

    /**
     * @author Kasualix
     * @reason avoid allocation
     */
    @Overwrite(remap = false)
    public void nextState() {
        if (getState().ordinal() + 1 < states_length) setState(ALL_STATES[getState().ordinal() + 1]);
    }
}

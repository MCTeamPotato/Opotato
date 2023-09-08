package com.teampotato.opotato.mixin.api;

import com.teampotato.opotato.api.IEntityTracker;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityTracker.class)
public abstract class MixinEntityTracker implements IEntityTracker {
    @Mutable @Shadow @Final private boolean trackEyeHeight;

    @Override
    public void setTrackEyeHeight(boolean trackEyeHeight) {
        this.trackEyeHeight = trackEyeHeight;
    }
}

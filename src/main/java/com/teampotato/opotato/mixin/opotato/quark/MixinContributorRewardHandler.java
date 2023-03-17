package com.teampotato.opotato.mixin.opotato.quark;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vazkii.quark.base.handler.ContributorRewardHandler;

@Mixin(value = ContributorRewardHandler.class, remap = false)
public class MixinContributorRewardHandler {
    /**
     * @author Doctor Who
     * @reason Saving The World
     */
    @Overwrite
    public static void init() {}
}

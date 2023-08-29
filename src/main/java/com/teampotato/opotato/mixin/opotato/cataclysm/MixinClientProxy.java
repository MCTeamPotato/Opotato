package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.ClientProxy;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ClientProxy.class, remap = false)
public abstract class MixinClientProxy {
}

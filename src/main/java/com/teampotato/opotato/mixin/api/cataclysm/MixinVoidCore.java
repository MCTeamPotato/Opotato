package com.teampotato.opotato.mixin.api.cataclysm;

import L_Ender.cataclysm.items.void_core;
import com.teampotato.opotato.api.cataclysm.IVoidCore;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = void_core.class, remap = false)
public abstract class MixinVoidCore implements IVoidCore {
    @Shadow protected abstract boolean spawnFangs(double x, double y, double z, int lowestYCheck, float rotationYaw, int warmupDelayTicks, Level world, Player player);

    @Override
    public boolean _spawnFangs(double x, double y, double z, int lowestYCheck, float rotationYaw, int warmupDelayTicks, Level world, Player player) {
        return this.spawnFangs(x, y, z, lowestYCheck, rotationYaw, warmupDelayTicks, world, player);
    }
}

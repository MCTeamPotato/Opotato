package com.teampotato.opotato.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface IVoidCore {
    boolean potato$spawnFangs(double x, double y, double z, int lowestYCheck, float rotationYaw, int warmupDelayTicks, Level world, Player player);
}

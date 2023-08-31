package com.teampotato.opotato.api;

import net.minecraft.world.phys.AABB;

public interface IAABB {
    AABB inflate(double value);
    AABB inflate(double x, double y, double z);
    AABB expandTowards(double x, double y, double z);
}

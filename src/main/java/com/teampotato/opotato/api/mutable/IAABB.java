package com.teampotato.opotato.api.mutable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("SpellCheckingInspection")
public interface IAABB {
    AABB _contract(double x, double y, double z);
    AABB _expandTowards(Vec3 vector);
    AABB _expandTowards(double x, double y, double z);
    AABB _inflate(double x, double y, double z);
    AABB _inflate(double value);
    AABB _intersect(AABB other);
    AABB _minmax(AABB other);
    AABB _move(double x, double y, double z);
    AABB _move(BlockPos pos);
    AABB _move(Vec3 vec);
    AABB _deflate(double value);
}
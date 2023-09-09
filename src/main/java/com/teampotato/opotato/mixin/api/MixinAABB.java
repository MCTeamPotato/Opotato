package com.teampotato.opotato.mixin.api;

import com.teampotato.opotato.api.mutable.IAABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AABB.class)
public abstract class MixinAABB implements IAABB {
    @Mutable @Shadow @Final public double minX;
    @Mutable @Shadow @Final public double minY;
    @Mutable @Shadow @Final public double minZ;
    @Mutable @Shadow @Final public double maxX;
    @Mutable @Shadow @Final public double maxY;
    @Mutable @Shadow @Final public double maxZ;

    @Override
    public AABB _contract(double x, double y, double z) {
        if (x < 0.0) {
            this.minX -= x;
        } else if (x > 0.0) {
            this.maxX -= x;
        }

        if (y < 0.0) {
            this.minY -= y;
        } else if (y > 0.0) {
            this.maxY -= y;
        }

        if (z < 0.0) {
            this.minZ -= z;
        } else if (z > 0.0) {
            this.maxZ -= z;
        }

        return (AABB) (Object) this;
    }


    @Override
    public AABB _expandTowards(@NotNull Vec3 vector) {
        return this._expandTowards(vector.x, vector.y, vector.z);
    }

    @Override
    public AABB _expandTowards(double x, double y, double z) {
        if (x < 0.0) {
            this.minX += x;
        } else if (x > 0.0) {
            this.maxX += x;
        }

        if (y < 0.0) {
            this.minY += y;
        } else if (y > 0.0) {
            this.maxY += y;
        }

        if (z < 0.0) {
            this.minZ += z;
        } else if (z > 0.0) {
            this.maxZ += z;
        }

        return (AABB) (Object) this;
    }

    @Override
    public AABB _inflate(double x, double y, double z) {
        this.minX = Math.min(this.minX - x, this.maxX + x);
        this.minY = Math.min(this.minY - y, this.maxY + y);
        this.minZ = Math.min(this.minZ - z, this.maxZ + z);
        this.maxX = Math.max(this.minX - x, this.maxX + x);
        this.maxY = Math.max(this.minY - y, this.maxY + y);
        this.maxZ = Math.max(this.minZ - z, this.maxZ + z);
        return (AABB) (Object) this;
    }

    @Override
    public AABB _inflate(double value) {
        return this._inflate(value, value, value);
    }

    @Override
    public AABB _intersect(@NotNull AABB other) {
        this.minX = Math.max(this.minX, other.minX);
        this.minY = Math.max(this.minY, other.minY);
        this.minZ = Math.max(this.minZ, other.minZ);
        this.maxX = Math.min(this.maxX, other.maxX);
        this.maxY = Math.min(this.maxY, other.maxY);
        this.maxZ = Math.min(this.maxZ, other.maxZ);
        return (AABB) (Object) this;
    }

    @Override
    public AABB _minmax(@NotNull AABB other) {
        this.minX = Math.min(this.minX, other.minX);
        this.minY = Math.min(this.minY, other.minY);
        this.minZ = Math.min(this.minZ, other.minZ);
        this.maxX = Math.max(this.maxX, other.maxX);
        this.maxY = Math.max(this.maxY, other.maxY);
        this.maxZ = Math.max(this.maxZ, other.maxZ);
        return (AABB) (Object) this;
    }

    @Override
    public AABB _move(double x, double y, double z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
        return (AABB) (Object) this;
    }

    @Override
    public AABB _move(@NotNull BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        this.minX += x;
        this.minY += y;
        this.minZ += z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
        return (AABB) (Object) this;
    }

    @Override
    public AABB _move(@NotNull Vec3 vec) {
        return this._move(vec.x, vec.y, vec.z);
    }

    @Override
    public AABB _deflate(double value) {
        return this._inflate(-value);
    }
}

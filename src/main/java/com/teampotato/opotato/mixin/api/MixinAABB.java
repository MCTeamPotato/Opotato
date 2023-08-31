package com.teampotato.opotato.mixin.api;

import com.teampotato.opotato.api.IAABB;
import net.minecraft.world.phys.AABB;
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
    public AABB inflate(double value) {
        this.minX = this.minX - value;
        this.minY = this.minY - value;
        this.minZ = this.minZ - value;
        this.maxX = this.maxX + value;
        this.maxY = this.maxY + value;
        this.maxZ = this.maxZ + value;
        return (AABB) (Object) this;
    }

    @Override
    public AABB inflate(double x, double y, double z) {
        this.minX = this.minX - x;
        this.minY = this.minY - y;
        this.minZ = this.minZ - z;
        this.maxX = this.maxX + x;
        this.maxY = this.maxY + y;
        this.maxZ = this.maxZ + z;
        return (AABB) (Object) this;
    }

    @Override
    public AABB expandTowards(double x, double y, double z) {
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
}

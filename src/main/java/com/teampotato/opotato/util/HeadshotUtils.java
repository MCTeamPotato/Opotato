package com.teampotato.opotato.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;

public class HeadshotUtils {
    public static boolean calculateIsHeadHit(Vec3 sourcePos, LivingEntity entity) {
        if (entity.getPose().equals(Pose.SWIMMING)) {
            return calculateComplexHeadHit(sourcePos, entity);
        } else {
            return calculateSimpleHeadHit(sourcePos, entity);
        }
    }

    static boolean calculateSimpleHeadHit(Vec3 sourcePos, LivingEntity entity) {
        double playerHeadStart = entity.position().add(0.0, entity.getDimensionsForge(entity.getPose()).height * 0.85F, 0.0).y - 0.17;
        return sourcePos.y > playerHeadStart;
    }

    static boolean calculateComplexHeadHit(Vec3 sourcePos, LivingEntity entity) {
        double headY = - Math.sin(Math.toRadians(entity.xRot));

        double lengthMod = Math.cos(entity.xRot);

        double headX = Math.sin(Math.toRadians(- entity.yRot)) * lengthMod;
        double headZ = Math.cos(Math.toRadians(- entity.yRot)) * lengthMod;
        return sourcePos.closerThan(entity.position().add(headX, headY / 2, headZ), 0.4);
    }
}

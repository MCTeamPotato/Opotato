package com.teampotato.opotato.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.util.math.vector.Vector3d;

public class HeadshotUtils {
    public static boolean calculateIsHeadHit(Vector3d sourcePos, LivingEntity entity) {
        if (entity.getPose().equals(Pose.SWIMMING)) {
            return calculateComplexHeadHit(sourcePos, entity);
        } else {
            return calculateSimpleHeadHit(sourcePos, entity);
        }
    }

    private static boolean calculateSimpleHeadHit(Vector3d sourcePos, LivingEntity entity) {
        double playerHeadStart = entity.position().add(0.0, entity.getDimensionsForge(entity.getPose()).height * 0.85F, 0.0).y - 0.17;
        return sourcePos.y > playerHeadStart;
    }

    private static boolean calculateComplexHeadHit(Vector3d sourcePos, LivingEntity entity) {
        double headY = - Math.sin(Math.toRadians(entity.xRot));

        double lengthMod = Math.cos(entity.xRot);

        double headX = Math.sin(Math.toRadians(- entity.yRot)) * lengthMod;
        double headZ = Math.cos(Math.toRadians(- entity.yRot)) * lengthMod;

        return sourcePos.closerThan(entity.position().add(headX, headY / 2, headZ), 0.4);
    }
}

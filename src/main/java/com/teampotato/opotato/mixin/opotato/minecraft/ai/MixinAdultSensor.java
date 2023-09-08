package com.teampotato.opotato.mixin.opotato.minecraft.ai;

import net.minecraft.world.entity.AgableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.AdultSensor;
import net.minecraft.world.entity.ai.sensing.Sensor;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;
import java.util.Optional;

@Mixin(AdultSensor.class)
public abstract class MixinAdultSensor extends Sensor<AgableMob> {
    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    private void setNearestVisibleAdult(AgableMob arg, @NotNull List<LivingEntity> livingEntities) {
        AgableMob nearestVisibleAdult = null;

        for (LivingEntity entity : livingEntities) {
            if (entity.getType() == arg.getType() && entity instanceof AgableMob) {
                AgableMob agableMob = (AgableMob) entity;
                if (!agableMob.isBaby()) {
                    nearestVisibleAdult = agableMob;
                    break;
                }
            }
        }

        arg.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT, Optional.ofNullable(nearestVisibleAdult));
    }
}

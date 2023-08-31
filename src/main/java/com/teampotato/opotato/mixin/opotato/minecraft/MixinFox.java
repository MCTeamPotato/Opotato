package com.teampotato.opotato.mixin.opotato.minecraft;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.concurrent.ThreadLocalRandom;

@Mixin(Fox.class)
public abstract class MixinFox extends Animal {
    protected MixinFox(EntityType<? extends Animal> arg, Level arg2) {
        super(arg, arg2);
    }

    @Shadow public abstract boolean isSleeping();

    /**
     * @author Kasualix
     * @reason The {@link net.minecraft.world.level.Level#getEntitiesOfClass(Class, AABB)} method is very slow, tbh.
     */
    @Overwrite
    @Nullable
    protected SoundEvent getAmbientSound() {
        if (this.isSleeping()) {
            return SoundEvents.FOX_SLEEP;
        } else {
            if (!this.level.isDay() && ThreadLocalRandom.current().nextFloat() < 0.1F) {
                for (Player player : level.players()) {
                    if (this.getBoundingBox().inflate(16.0, 16.0, 16.0).contains(player.position()) && !player.isSpectator()) return SoundEvents.FOX_AMBIENT;
                }
                return SoundEvents.FOX_SCREECH;
            }
            return SoundEvents.FOX_AMBIENT;
        }
    }
}

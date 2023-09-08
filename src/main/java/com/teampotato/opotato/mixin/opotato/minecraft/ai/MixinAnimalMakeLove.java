package com.teampotato.opotato.mixin.opotato.minecraft.ai;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.Animal;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Optional;

@Mixin(AnimalMakeLove.class)
public abstract class MixinAnimalMakeLove {
    @Shadow @Final private EntityType<? extends Animal> partnerType;

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    private Optional<? extends Animal> findValidBreedPartner(@NotNull Animal animal) {
        Optional<List<LivingEntity>> memoryModuleType = animal.getBrain().getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES);
        if (!memoryModuleType.isPresent()) return Optional.empty();
        for (LivingEntity entity : memoryModuleType.get()) {
            if (entity instanceof Animal) {
                Animal partnerCandidate = (Animal) entity;
                if (partnerCandidate.getType() == this.partnerType && animal.canMate(partnerCandidate)) return Optional.of(partnerCandidate);
            }
        }

        return Optional.empty();
    }
}

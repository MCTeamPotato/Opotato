package com.teampotato.opotato.mixin.opotato.minecraft.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Optional;

@Mixin(Zoglin.class)
public class MixinZoglin extends Monster {
    protected MixinZoglin(EntityType<? extends Monster> arg, Level arg2) {
        super(arg, arg2);
    }

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    private Optional<? extends LivingEntity> findNearestValidAttackTarget() {
        for (LivingEntity livingEntity : this.getBrain().getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(ImmutableList.of())) {
            if (Zoglin.isTargetable(livingEntity)) return Optional.of(livingEntity);
        }
        return Optional.empty();
    }
}

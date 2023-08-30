package com.teampotato.opotato.mixin.opotato.witherstorm;

import com.teampotato.opotato.events.WitherStormCaches;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.common.capability.WitherStormBowelsManager;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class MixinMCLivingEntity extends Entity {
    @Shadow public abstract <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing);

    public MixinMCLivingEntity(EntityType<?> arg, Level arg2) {
        super(arg, arg2);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (this.level instanceof ServerLevel) {
            boolean isBowels = this.level.dimension().location().equals(WitherStormMod.bowelsLocation());
            if (this.getY() < 50.0 && isBowels) WitherStormBowelsManager.leave((ServerLevel) this.level, (LivingEntity)(Object) this, null);
            ServerLevel serverLevel = (ServerLevel) this.level;
            this.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER).ifPresent(tracker -> {
                if (!tracker.isActuallyImmune()) {
                    boolean nearby = isBowels;
                    if (!nearby) {
                        for (UUID uuid : WitherStormCaches.witherStorms.keySet()) {
                            WitherStormEntity witherStormEntity = (WitherStormEntity) serverLevel.getEntity(uuid);
                            if (witherStormEntity != null && witherStormEntity.getPhase() > 1) {
                                nearby = witherStormEntity.isEntityNearby((LivingEntity) (Object) this);
                                if (nearby) break;
                            }
                        }
                    }
                    tracker.setNearStorm(nearby);
                }
                tracker.tick();
            });
        }
    }
}
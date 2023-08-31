package com.teampotato.opotato.mixin.opotato.minecraft;

import com.teampotato.opotato.api.IAABB;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(BeaconBlockEntity.class)
public abstract class MixinBeaconBlockEntity extends BlockEntity {
    @Shadow @Nullable private MobEffect primaryPower;

    @Shadow private int levels;

    @Shadow @Nullable private MobEffect secondaryPower;

    public MixinBeaconBlockEntity(BlockEntityType<?> arg) {
        super(arg);
    }

    /**
     * @author Kasualix
     * @reason The {@link net.minecraft.world.level.Level#getEntitiesOfClass(Class, AABB)} method is very slow, tbh, and avoid AABB allocation
     */
    @Overwrite
    private void applyEffects() {
        if (this.level != null && !this.level.isClientSide && this.primaryPower != null) {
            double d0 = this.levels * 10 + 10;
            int i = 0;
            if (this.levels >= 4 && this.primaryPower == this.secondaryPower) {
                i = 1;
            }

            int j = (9 + this.levels * 2) * 20;

            AABB box = ((IAABB)((IAABB)new AABB(this.worldPosition)).inflate(d0)).expandTowards(0.0, this.level.getMaxBuildHeight(), 0.0);

            for (Player player : level.players()) {
                if (box.contains(player.position())) {
                    player.addEffect(new MobEffectInstance(this.primaryPower, j, i, true, true));

                    if (this.levels >= 4 && this.primaryPower != this.secondaryPower && this.secondaryPower != null) {
                        player.addEffect(new MobEffectInstance(this.secondaryPower, j, 0, true, true));
                    }
                }
            }
        }
    }
}

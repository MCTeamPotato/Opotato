package com.teampotato.opotato.mixin.opotato.minecraft;

import com.teampotato.opotato.api.IAABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ConduitBlockEntity.class)
public abstract class MixinConduitTileEntity extends BlockEntity {
    @Shadow @Final private List<BlockPos> effectBlocks;

    public MixinConduitTileEntity(BlockEntityType<?> arg) {
        super(arg);
    }

    /**
     * @author Kasualix
     * @reason The {@link net.minecraft.world.level.Level#getEntitiesOfClass(Class, AABB)} method is very slow, tbh.
     */
    @Overwrite
    private void applyEffects() {
        if (this.level == null) return;
        int effectBlocksSize = this.effectBlocks.size();
        int j = effectBlocksSize / 7 * 16;
        int x = this.worldPosition.getX();
        int y = this.worldPosition.getY();
        int z = this.worldPosition.getZ();
        AABB box = ((IAABB)((IAABB) new AABB(x, y, z, x + 1, y + 1, z + 1)).inflate(j)).expandTowards(0.0, this.level.getMaxBuildHeight(), 0.0);
        if (!level.players().isEmpty()) {
            for (Player player : level.players()) {
                if (this.worldPosition.closerThan(player.blockPosition(), j) && player.isInWaterOrRain() && box.contains(player.position())) {
                    player.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 260, 0, true, true));
                }
            }
        }
    }
}

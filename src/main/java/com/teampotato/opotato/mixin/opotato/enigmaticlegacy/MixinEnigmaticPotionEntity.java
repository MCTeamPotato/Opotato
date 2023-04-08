package com.teampotato.opotato.mixin.opotato.enigmaticlegacy;

import com.integral.enigmaticlegacy.entities.EnigmaticPotionEntity;
import com.integral.enigmaticlegacy.helpers.PotionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(value = EnigmaticPotionEntity.class, remap = false)
public abstract class MixinEnigmaticPotionEntity extends ThrowableEntity {

    protected MixinEnigmaticPotionEntity(EntityType<? extends ThrowableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract ItemStack getItem();

    @Shadow
    protected abstract boolean isLingering();

    @Shadow
    protected abstract void makeAreaOfEffectCloud(ItemStack stack, List<EffectInstance> list);

    @Shadow
    protected abstract void triggerSplash(List<EffectInstance> p_213888_1_, @Nullable Entity p_213888_2_);

    /**
     * @author Doctor Who
     * @reason Saving The World
     */
    @Overwrite
    @Override
    protected void onHit(RayTraceResult result) {
        if (this.level.isClientSide) return;
        ItemStack itemstack = this.getItem();
        List<EffectInstance> list = PotionHelper.getEffects(itemstack);
        int i = 2002;
        if (list != null && !list.isEmpty()) {
            if (this.isLingering()) {
                this.makeAreaOfEffectCloud(itemstack, list);
            } else {
                this.triggerSplash(list, result.getType() == RayTraceResult.Type.ENTITY ? ((EntityRayTraceResult)result).getEntity() : null);
            }
            if (list.stream().anyMatch(instance -> instance.getEffect().isInstantenous())) i = 2007;
        }
        this.level.levelEvent(i, new BlockPos(this.blockPosition()), PotionHelper.getColor(itemstack));
        this.remove();
    }
}

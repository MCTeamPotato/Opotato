package com.teampotato.opotato.mixin.opotato.blueskies;

import com.legacy.blue_skies.entities.hostile.boss.ArachnarchEntity;
import com.legacy.blue_skies.entities.util.base.SkiesBossEntity;
import com.legacy.blue_skies.entities.util.interfaces.IStunnableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(ArachnarchEntity.class)
public abstract class MixinArachnarchEntity extends SkiesBossEntity implements RangedAttackMob, IStunnableMob {
    @Shadow public static Set<Item> SHIELDS;

    @Shadow public abstract void setStunned(boolean stunned);

    public MixinArachnarchEntity(EntityType<? extends SkiesBossEntity> type, net.minecraft.world.level.Level worldIn) {
        super(type, worldIn);
    }

    /**
     * @author Lifesaver
     * @reason f**k this sh*t
     */
    @Overwrite
    public boolean doHurtTarget(Entity entityIn) {
        if (this.isAlliedTo(entityIn)) {
            return false;
        } else {
            if (entityIn instanceof Player) {
                Player player = (Player)entityIn;
                ItemStack playerItem = player.isUsingItem() ? player.getUseItem() : ItemStack.EMPTY;
                if (!playerItem.isEmpty() && !this.level.isClientSide) {
                    if (SHIELDS.contains(playerItem.getItem()) || playerItem.isShield(player)) {
                        this.level.broadcastEntityEvent(this, (byte)4);
                        this.setStunned(true);
                        this.level.broadcastEntityEvent(player, (byte)29);
                        this.level.broadcastEntityEvent(player, (byte)30);
                        player.disableShield(true);
                        player.getCooldowns().addCooldown(playerItem.getItem(), 800);
                        SHIELDS.forEach((item) -> player.getCooldowns().addCooldown(item.getItem(), 300));
                        if (player.getUsedItemHand() != null) {
                            playerItem.hurtAndBreak(1, player, (e) -> e.broadcastBreakEvent(player.getUsedItemHand()));
                        }

                        return false;
                    }
                }
            }

            return super.doHurtTarget(entityIn);
        }
    }
}

package com.teampotato.opotato.mixin.opotato.blueskies;

import com.legacy.blue_skies.entities.hostile.boss.ArachnarchEntity;
import com.legacy.blue_skies.entities.util.base.SkiesBossEntity;
import com.legacy.blue_skies.entities.util.interfaces.IStunnableMob;
import com.teampotato.opotato.config.mods.BlueSkiesExtraConfig;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(ArachnarchEntity.class)
public abstract class MixinArachnarchEntity extends SkiesBossEntity implements RangedAttackMob, IStunnableMob {
    @Shadow public abstract boolean isAlliedTo(Entity entityIn);

    @Shadow(remap = false) public static Set<Item> SHIELDS;

    @Shadow(remap = false) public abstract void setStunned(boolean stunned);

    public MixinArachnarchEntity(EntityType<? extends SkiesBossEntity> type, net.minecraft.world.level.Level worldIn) {
        super(type, worldIn);
    }

    /**
     * @author Lifesaver
     * @reason F**k this sh*t
     */
    @Overwrite
    public boolean doHurtTarget(Entity entityIn) {
        if (this.isAlliedTo(entityIn)) return false;
        if (entityIn instanceof Player) {
            Player player = (Player) entityIn;
            ItemStack playerUseItem = player.isUsingItem() ? player.getUseItem() : ItemStack.EMPTY;
            if (!playerUseItem.isEmpty() && !this.level.isClientSide()) {
                if (!BlueSkiesExtraConfig.arachnarchShieldNerf.get()) {
                    if (SHIELDS.contains(playerUseItem.getItem()) || playerUseItem.isShield(player)) {
                        this.level.broadcastEntityEvent(this, (byte) 4);
                        this.setStunned(true);
                        this.level.broadcastEntityEvent(this, (byte) 29);
                        this.level.broadcastEntityEvent(this, (byte) 30);
                        player.disableShield(true);
                        player.getCooldowns().addCooldown(ForgeRegistries.ITEMS.getValue(playerUseItem.getItem().getRegistryName()),300);
                        playerUseItem.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(player.getUsedItemHand()));
                        return false;
                    }
                } else {
                    if (SHIELDS.contains(playerUseItem.getItem())) {
                        this.level.broadcastEntityEvent(this, (byte)4);
                        this.setStunned(true);
                        this.level.broadcastEntityEvent(player, (byte)29);
                        this.level.broadcastEntityEvent(player, (byte)30);
                        player.disableShield(true);
                        SHIELDS.forEach((item) -> player.getCooldowns().addCooldown(item.getItem(), 300));
                        if (player.getUsedItemHand() != null) {
                            playerUseItem.hurtAndBreak(1, player, (e) -> e.broadcastBreakEvent(player.getUsedItemHand()));
                        }

                        return false;
                    }

                    if (playerUseItem.isShield(player)) {
                        player.displayClientMessage(new TranslatableComponent("gui.blue_skies.tooltip.arachnarch_incorrect_shield"), true);
                        player.disableShield(true);
                        this.level.broadcastEntityEvent(player, (byte)30);
                        player.getCooldowns().addCooldown(playerUseItem.getItem(), 800);
                        return super.doHurtTarget(entityIn);
                    }
                }
            }
        }

        return super.doHurtTarget(entityIn);
    }
}

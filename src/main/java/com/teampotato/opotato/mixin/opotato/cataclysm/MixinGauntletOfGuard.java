package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.Gauntlet_of_Guard;
import com.teampotato.opotato.config.mods.CataclysmExtraConfig;
import com.teampotato.opotato.config.mods.CataclysmExtraJsonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gauntlet_of_Guard.class)
public abstract class MixinGauntletOfGuard extends Item {
    public MixinGauntletOfGuard(Properties arg) {
        super(arg);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(Properties group, CallbackInfo ci) {
        if (CataclysmExtraJsonConfig.gauntletOfGuardDamageable) ((ItemAccessor)this).setMaxDamage(CataclysmExtraJsonConfig.gauntletOfGuardDurability);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (CataclysmExtraJsonConfig.gauntletOfGuardDamageable) {
            stack.hurtAndBreak(1, attacker, user -> user.broadcastBreakEvent(attacker.getUsedItemHand()));
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (state.getDestroySpeed(level, pos) != 0.0F) {
            stack.hurtAndBreak(2, miningEntity, arg -> arg.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }

        return true;
    }

    /**
     * @author Kasualix
     * @reason impl config
     */
    @Overwrite
    public UseAnim getUseAnimation(ItemStack p_77661_1_) {
        return UseAnim.valueOf(CataclysmExtraConfig.gauntletOfGuardUseAnimation.get());
    }

    /**
     * @author Kasualix
     * @reason impl config
     */
    @Overwrite
    public int getEnchantmentValue() {
        return CataclysmExtraConfig.gauntletOfGuardEnchantmentValue.get();
    }

    /**
     * @author Kasualix
     * @reason impl config
     */
    @Overwrite
    public boolean isEnchantable(ItemStack stack) {
        return CataclysmExtraConfig.gauntletOfGuardIsEnchantable.get();
    }

    /**
     * @author Kasualix
     * @reason impl config
     */
    @Overwrite
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return CataclysmExtraConfig.canGauntletOfGuardDisableShield.get();
    }

    @ModifyConstant(method = "onUsingTick", constant = @Constant(doubleValue = 11.0))
    private double onUsingTick(double constant) {
        return CataclysmExtraConfig.gauntletOfGuardEntitiesDetectionRadius.get();
    }
}

package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.Gauntlet_of_Guard;
import com.teampotato.opotato.config.mods.CataclysmExtraConfig;
import com.teampotato.opotato.config.mods.CataclysmExtraJsonConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Gauntlet_of_Guard.class)
public abstract class MixinGauntletOfGuard extends Item {
    public MixinGauntletOfGuard(Properties arg) {
        super(arg);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(Properties group, CallbackInfo ci) {
        if (CataclysmExtraJsonConfig.gauntletOfGuardDamageable) ((ItemAccessor)this).setMaxDamage(CataclysmExtraJsonConfig.gauntletOfGuardDurability);
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;startUsingItem(Lnet/minecraft/world/InteractionHand;)V"))
    private void onUse(Level world, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (CataclysmExtraJsonConfig.gauntletOfGuardDamageable) {
            player.getItemInHand(hand).hurt(1, random, player instanceof ServerPlayer ? (ServerPlayer) player : null);
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (CataclysmExtraJsonConfig.gauntletOfGuardDamageable) {
            stack.hurt(1, random, attacker instanceof ServerPlayer ? (ServerPlayer) attacker : null);
        }
        return super.hurtEnemy(stack, target, attacker);
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

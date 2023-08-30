package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.The_Incinerator;
import com.teampotato.opotato.api.IItem;
import com.teampotato.opotato.config.mods.CataclysmExtraConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(The_Incinerator.class)
public abstract class MixinTheIncinerator extends Item {
    public MixinTheIncinerator(Properties arg) {
        super(arg);
    }

    @Override
    public Rarity getRarity(ItemStack arg) {
        return Rarity.EPIC;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(Properties group, CallbackInfo ci) {
        if (CataclysmExtraConfig.incineratorCanBeDamaged.get()) {
            ((IItem)this).potato$setMaxDamage(CataclysmExtraConfig.incineratorDurability.get());
        }
    }

    @ModifyConstant(method = "releaseUsing", constant = @Constant(intValue = 60))
    private int onRelease(int constant) {
        return CataclysmExtraConfig.incineratorChargeTicks.get();
    }

    @ModifyConstant(method = "onUsingTick", constant = @Constant(intValue = 60))
    private int onUsingTick(int constant) {
        return CataclysmExtraConfig.incineratorChargeTicks.get();
    }

    @Inject(method = "onUsingTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", shift = At.Shift.AFTER))
    private void displayMessage(ItemStack stack, LivingEntity player, int count, CallbackInfo ci) {
        if (CataclysmExtraConfig.showStatusMessageWhenIncineratorFlameStrikeIsReady.get() && player instanceof Player) {
            ((Player)player).displayClientMessage(new TranslatableComponent("opotato.catatclysm.incinerator.ready"), true);
        }
    }

    /**
     * @author Kasualix
     * @reason impl config
     */
    @Overwrite
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.valueOf(CataclysmExtraConfig.incineratorAnimationWhenCharging.get());
    }

    /**
     * @author Kasualix
     * @reason impl config
     */
    @Overwrite
    public int getEnchantmentValue() {
        return CataclysmExtraConfig.infernalForgeEnchantmentValue.get();
    }

    @Inject(method = "canApplyAtEnchantingTable", at = @At("HEAD"), cancellable = true, remap = false)
    private void onCheckEnchant(ItemStack stack, Enchantment enchantment, CallbackInfoReturnable<Boolean> cir) {
        if (CataclysmExtraConfig.incineratorCanBeDamaged.get()) {
            cir.setReturnValue((super.canApplyAtEnchantingTable(stack, enchantment) || enchantment.category == EnchantmentCategory.BREAKABLE || enchantment.category == EnchantmentCategory.WEAPON) && enchantment != Enchantments.SWEEPING_EDGE);
            cir.cancel();
        }
    }

    @ModifyConstant(method = "releaseUsing", constant = @Constant(intValue = 400))
    private int onSetCoolDown(int constant) {
        return CataclysmExtraConfig.incineratorCoolDownTicks.get();
    }

    @ModifyConstant(method = "releaseUsing", constant = @Constant(intValue = 40))
    private int onGetFlameStrikeDuration(int constant) {
        return CataclysmExtraConfig.flameStrikeSummonedByIncineratorDuration.get();
    }

    @ModifyConstant(method = "releaseUsing", constant = @Constant(floatValue = 1.0F, ordinal = 0))
    private float onGetFlameStrikeRadius(float constant) {
        return CataclysmExtraConfig.flameStrikeSummonedByIncineratorRadius.get().floatValue();
    }

    /**
     * @author Kasualix
     * @reason adapt charge ticks config
     */
    @Overwrite
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add((new TranslatableComponent("opotato.cataclysm.incinerator.desc", CataclysmExtraConfig.incineratorChargeTicks.get())).withStyle(ChatFormatting.DARK_GREEN));
        tooltip.add((new TranslatableComponent("item.cataclysm.incinerator2.desc")).withStyle(ChatFormatting.DARK_GREEN));
        tooltip.add((new TranslatableComponent("item.cataclysm.incinerator3.desc")).withStyle(ChatFormatting.DARK_GREEN));
    }
}

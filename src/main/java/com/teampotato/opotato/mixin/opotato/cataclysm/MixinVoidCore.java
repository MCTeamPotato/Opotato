package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.void_core;
import com.teampotato.opotato.api.IItem;
import com.teampotato.opotato.config.mods.CataclysmExtraConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

@Mixin(void_core.class)
public abstract class MixinVoidCore extends Item implements ICurioItem {
    public MixinVoidCore(Properties arg) {
        super(arg);
    }

   @ModifyConstant(method = "use", constant = @Constant(intValue = 120))
    private int onSetCoolDown(int constant) {
        return CataclysmExtraConfig.voidCoreCoolDown.get();
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemCooldowns;addCooldown(Lnet/minecraft/world/item/Item;I)V",shift = At.Shift.AFTER))
    private void reduceDurability(Level world, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (CataclysmExtraConfig.voidCoreCanBeDamaged.get()) {
            ItemStack stack = player.getItemInHand(hand);
            int damage = stack.getDamageValue();
            if (damage + 1 >= CataclysmExtraConfig.voidCoreDurability.get()) {
                stack.shrink(1);
            } else {
                this.setDamage(stack, damage + 1);
            }
        }
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(Properties group, CallbackInfo ci) {
        if (CataclysmExtraConfig.voidCoreCanBeDamaged.get()) ((IItem)this)._setMaxDamage(CataclysmExtraConfig.voidCoreDurability.get());
    }
}

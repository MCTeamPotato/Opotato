package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.void_core;
import com.teampotato.opotato.config.mods.CataclysmExtraConfig;
import com.teampotato.opotato.config.mods.CataclysmExtraJsonConfig;
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

@Mixin(void_core.class)
public abstract class MixinVoidCore extends Item {
    public MixinVoidCore(Properties arg) {
        super(arg);
    }

    @ModifyConstant(method = "use", constant = @Constant(intValue = 120))
    private int onSetCoolDown(int constant) {
        return CataclysmExtraConfig.voidCoreCoolDown.get();
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(Properties group, CallbackInfo ci) {
        if (CataclysmExtraJsonConfig.voidCoreDamageable) ((ItemAccessor)this).setMaxDamage(CataclysmExtraJsonConfig.voidCoreDurability);
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemCooldowns;addCooldown(Lnet/minecraft/world/item/Item;I)V"))
    private void onUse(Level world, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (CataclysmExtraJsonConfig.voidCoreDamageable) {
            this.setDamage(player.getItemInHand(hand), this.getDamage(player.getItemInHand(hand)) + 1);
        }
    }
}

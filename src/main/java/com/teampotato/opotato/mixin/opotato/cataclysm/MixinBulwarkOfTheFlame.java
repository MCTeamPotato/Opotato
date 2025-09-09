package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.Bulwark_of_the_flame;
import com.teampotato.opotato.config.mods.CataclysmExtraJsonConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bulwark_of_the_flame.class)
public abstract class MixinBulwarkOfTheFlame extends Item {
    public MixinBulwarkOfTheFlame(Properties arg) {
        super(arg);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(Properties group, CallbackInfo ci) {
        if (CataclysmExtraJsonConfig.bulwarkOfTheFlameDamageable) ((ItemAccessor)this).setMaxDamage(CataclysmExtraJsonConfig.bulwarkOfTheFlameDurability);
    }

    @Inject(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemCooldowns;addCooldown(Lnet/minecraft/world/item/Item;I)V"))
    private void onUse(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft, CallbackInfo ci) {
        if (CataclysmExtraJsonConfig.bulwarkOfTheFlameDamageable) stack.hurt(1, random, entityLiving instanceof ServerPlayer ? (ServerPlayer) entityLiving : null);
    }
}

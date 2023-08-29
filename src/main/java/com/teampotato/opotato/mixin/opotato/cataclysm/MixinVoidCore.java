package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.void_core;
import com.teampotato.opotato.api.IVoidCore;
import com.teampotato.opotato.config.CataclysmExtraConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

@Mixin(void_core.class)
public abstract class MixinVoidCore extends Item implements ICurioItem, IVoidCore {
    public MixinVoidCore(Properties arg) {
        super(arg);
    }

    @Shadow protected abstract boolean spawnFangs(double x, double y, double z, int lowestYCheck, float rotationYaw, int warmupDelayTicks, Level world, Player player);

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

    @Override
    public boolean potato$spawnFangs(double x, double y, double z, int lowestYCheck, float rotationYaw, int warmupDelayTicks, Level world, Player player) {
        return this.spawnFangs(x, y, z, lowestYCheck, rotationYaw, warmupDelayTicks, world, player);
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;<init>(Lnet/minecraft/world/item/Item$Properties;)V"))
    private static Item.Properties modifyProperties(Item.Properties arg) {
        if (CataclysmExtraConfig.voidCoreCanBeDamaged.get()) {
            return arg.durability(CataclysmExtraConfig.voidCoreDurability.get());
        } else {
            return arg;
        }
    }
}

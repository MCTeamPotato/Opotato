package com.teampotato.opotato.mixin.opotato.blueskies;

import com.legacy.blue_skies.entities.hostile.boss.summons.ent.EntWallEntity;
import com.legacy.blue_skies.items.tools.SkyAxeItem;
import com.teampotato.opotato.config.mods.BlueSkiesExtraConfig;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntWallEntity.class)
public abstract class MixinEntWallEntity extends LivingEntity{
    @Shadow(remap = false) public abstract void playDamageEffect();

    protected MixinEntWallEntity(EntityType<? extends LivingEntity> arg, Level arg2) {
        super(arg, arg2);
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source == DamageSource.OUT_OF_WORLD) {
            cir.setReturnValue(super.hurt(source, amount));
        } else {
            if (source.getDirectEntity() instanceof LivingEntity) {
                ItemStack stack = ((LivingEntity)source.getDirectEntity()).getMainHandItem();
                if (BlueSkiesExtraConfig.onlySkyAxeHurtEntWall.get()) {
                    if (stack.getItem() instanceof SkyAxeItem) {
                        this.playDamageEffect();
                        cir.setReturnValue(super.hurt(source, amount));
                        return;
                    }
                } else {
                    if (stack.getItem() instanceof AxeItem) {
                        this.playDamageEffect();
                        cir.setReturnValue(super.hurt(source, amount));
                        return;
                    }
                }

                if (source.getDirectEntity() instanceof Player) {
                    ((Player)source.getDirectEntity()).displayClientMessage(new TranslatableComponent("gui.blue_skies.tooltip.invalid_ent_weapon"), true);
                }
            }

            cir.setReturnValue(false);
        }
    }
}

package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.items.Gauntlet_of_Guard;
import com.teampotato.opotato.config.mods.CataclysmExtraConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Gauntlet_of_Guard.class)
public abstract class MixinGauntletOfGuard extends Item {
    public MixinGauntletOfGuard(Properties arg) {
        super(arg);
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

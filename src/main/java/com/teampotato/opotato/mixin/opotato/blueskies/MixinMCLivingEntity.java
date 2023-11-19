package com.teampotato.opotato.mixin.opotato.blueskies;

import com.teampotato.opotato.config.mods.BlueSkiesExtraConfig;
import com.teampotato.opotato.util.blueskies.NerfHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinMCLivingEntity extends Entity {
    public MixinMCLivingEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Shadow public abstract double getAttributeValue(Attribute attribute);

    @SuppressWarnings("ConstantValue")
    @Inject(method = "getArmorValue", at = @At("HEAD"), cancellable = true)
    private void onGetArmorValue(CallbackInfoReturnable<Integer> cir) {
        int returnedArmorValue = Mth.floor(this.getAttributeValue(Attributes.ARMOR));
        if (BlueSkiesExtraConfig.enableDimensionalNerf.get()) {
            if (!BlueSkiesExtraConfig.enableEnhancedDimensionalNerf.get()) return;
            if (((Object)this) instanceof Player) NerfHelper.enhancedNerfInBlueSkiesDims((Player) (Object) this, returnedArmorValue, cir, this.level.dimension());
        } else {
            cir.setReturnValue(returnedArmorValue);
        }
    }
}

package com.teampotato.opotato.mixin.opotato.witherstorm;

import com.teampotato.opotato.Opotato;
import net.minecraft.world.entity.EquipmentSlot;
import nonamecrackers2.witherstormmod.common.entity.AbstractSickenedEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractSickenedEntity.class)
public abstract class MixinAbstractSickenedEntity {
    @Redirect(method = "finishConversion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EquipmentSlot;values()[Lnet/minecraft/world/entity/EquipmentSlot;"), remap = false)
    private EquipmentSlot[] avoidEnumValuesNoRemap() {
        return Opotato.EQUIPMENT_SLOTS;
    }

    @Redirect(method = "convertTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EquipmentSlot;values()[Lnet/minecraft/world/entity/EquipmentSlot;"))
    private EquipmentSlot[] avoidEnumValues() {
        return Opotato.EQUIPMENT_SLOTS;
    }
}

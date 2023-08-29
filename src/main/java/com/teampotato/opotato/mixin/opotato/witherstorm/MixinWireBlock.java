package com.teampotato.opotato.mixin.opotato.witherstorm;

import com.teampotato.opotato.Opotato;
import net.minecraft.core.Direction;
import nonamecrackers2.witherstormmod.common.block.WireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WireBlock.class)
public abstract class MixinWireBlock {
    @Redirect(method = "checkCornerChangeAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Direction;values()[Lnet/minecraft/core/Direction;"), remap = false)
    private Direction[] avoidEnumValuesNoRemap() {
        return Opotato.DIRECTIONS;
    }

    @Redirect(method = "onRemove", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Direction;values()[Lnet/minecraft/core/Direction;"))
    private Direction[] avoidEnumValues() {
        return Opotato.DIRECTIONS;
    }
}

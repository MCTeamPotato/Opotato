package com.teampotato.opotato.mixin.opotato.witherstorm;

import com.teampotato.opotato.Opotato;
import net.minecraft.core.Direction;
import nonamecrackers2.witherstormmod.client.util.BlockRenderingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockRenderingUtil.class)
public abstract class MixinBlockRenderingUtil {
    @Redirect(method = "renderModelFlat", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Direction;values()[Lnet/minecraft/core/Direction;"))
    private static Direction[] avoidEnumValues() {
        return Opotato.DIRECTIONS;
    }
}

package com.teampotato.opotato.mixin.opotato.blueskies;

import com.legacy.blue_skies.events.SkiesPlayerEvents;
import com.legacy.blue_skies.registries.SkiesDimensions;
import com.teampotato.opotato.config.mods.BlueSkiesExtraConfig;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SkiesPlayerEvents.class)
public abstract class MixinSkiesPlayerEvents {
    @Redirect(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lcom/legacy/blue_skies/registries/SkiesDimensions;inSkyDimension(Lnet/minecraft/world/entity/Entity;)Z"))
    private static boolean onCheck(Entity entity) {
        return BlueSkiesExtraConfig.enableEnhancedDimensionalNerf.get() && SkiesDimensions.inSkyDimension(entity);
    }
}

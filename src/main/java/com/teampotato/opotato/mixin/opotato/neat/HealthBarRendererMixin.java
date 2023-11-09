package com.teampotato.opotato.mixin.opotato.neat;

import com.teampotato.opotato.api.entity.NeatConfigChecker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.neat.HealthBarRenderer;

import java.util.List;

@Mixin(value = HealthBarRenderer.class, remap = false)
public abstract class HealthBarRendererMixin {
    @Redirect(method = "renderHealthBar", at = @At(value = "INVOKE", target = "Ljava/util/List;contains(Ljava/lang/Object;)Z", remap = false))
    private boolean fastCheck(List<String> instance, Object o) {
        String entityID = (String) o;
        EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityID));
        return entityType != null && ((NeatConfigChecker)entityType).potato$isInNeatBlacklist();
    }
}

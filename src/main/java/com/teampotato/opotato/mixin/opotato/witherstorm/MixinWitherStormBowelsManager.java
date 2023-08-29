package com.teampotato.opotato.mixin.opotato.witherstorm;

import com.teampotato.opotato.events.WitherStormCaches;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import nonamecrackers2.witherstormmod.common.capability.WitherStormBowelsManager;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(value = WitherStormBowelsManager.class, remap = false)
public abstract class MixinWitherStormBowelsManager {
    @Shadow @Final private ServerLevel world;

    /**
     * @author Kasualix
     * @reason impl cache
     */
    @Overwrite
    @Nullable
    private WitherStormEntity findStorm(UUID uuid) {
        for (UUID witherStormUUID : WitherStormCaches.witherStorms.keySet()) {
            ResourceKey<Level> resourceKey = WitherStormCaches.witherStorms.get(witherStormUUID);
            ServerLevel serverLevel = this.world.getServer().getLevel(resourceKey);
            if (witherStormUUID.equals(uuid) && serverLevel != null) return (WitherStormEntity) serverLevel.getEntity(witherStormUUID);
        }
        return null;
    }

    @Inject(method = "onWorldTick", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/util/LazyOptional;ifPresent(Lnet/minecraftforge/common/util/NonNullConsumer;)V", remap = false, shift = At.Shift.AFTER), cancellable = true)
    private static void onWorldTick(TickEvent.WorldTickEvent event, CallbackInfo ci) {
        ci.cancel();
    }
}

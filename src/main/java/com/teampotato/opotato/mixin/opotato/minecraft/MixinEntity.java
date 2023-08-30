package com.teampotato.opotato.mixin.opotato.minecraft;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(value = Entity.class, priority = 1)
public abstract class MixinEntity {
    @Mutable @Shadow @Final private Set<String> tags;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void optimizeTags(EntityType<?> arg, Level arg2, CallbackInfo ci) {
        this.tags = new ObjectOpenHashSet<>(this.tags);
    }
}

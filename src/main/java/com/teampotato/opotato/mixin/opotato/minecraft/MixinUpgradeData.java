package com.teampotato.opotato.mixin.opotato.minecraft;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.world.level.chunk.UpgradeData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(UpgradeData.class)
public abstract class MixinUpgradeData {
    @Mutable @Shadow @Final private static Set<UpgradeData.BlockFixer> CHUNKY_FIXERS;

    @Inject(method = {"<init>()V", "<init>(Lnet/minecraft/nbt/CompoundTag;)V"}, at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        CHUNKY_FIXERS = new ObjectOpenHashSet<>(CHUNKY_FIXERS);
    }
}

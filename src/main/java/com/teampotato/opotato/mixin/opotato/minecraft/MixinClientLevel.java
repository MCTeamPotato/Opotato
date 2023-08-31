package com.teampotato.opotato.mixin.opotato.minecraft;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class MixinClientLevel {
    @Mutable @Shadow @Final private List<AbstractClientPlayer> players;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(ClientPacketListener arg, ClientLevel.ClientLevelData arg2, ResourceKey<Level> arg3, DimensionType arg4, int i, Supplier<ProfilerFiller> supplier, LevelRenderer arg5, boolean bl, long l, CallbackInfo ci) {
        this.players = new ObjectArrayList<>(this.players);
    }
}

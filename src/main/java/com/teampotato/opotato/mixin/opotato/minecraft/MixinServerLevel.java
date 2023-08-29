package com.teampotato.opotato.mixin.opotato.minecraft;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;

@Mixin(ServerLevel.class)
public abstract class MixinServerLevel {
    @Mutable @Shadow @Final private Map<UUID, Entity> entitiesByUuid;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(MinecraftServer minecraftServer, Executor executor, LevelStorageSource.LevelStorageAccess arg, ServerLevelData arg2, ResourceKey<Level> arg3, DimensionType arg4, ChunkProgressListener arg5, ChunkGenerator arg6, boolean bl, long l, List<CustomSpawner> list, boolean bl2, CallbackInfo ci) {
        this.entitiesByUuid = new Object2ObjectOpenHashMap<>(this.entitiesByUuid);
    }
}

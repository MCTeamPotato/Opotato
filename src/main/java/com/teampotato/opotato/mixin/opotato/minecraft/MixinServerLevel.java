package com.teampotato.opotato.mixin.opotato.minecraft;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Mixin(ServerLevel.class)
public abstract class MixinServerLevel extends Level {
    @Mutable @Shadow @Final private List<ServerPlayer> players;

    protected MixinServerLevel(WritableLevelData arg, ResourceKey<Level> arg2, DimensionType arg3, Supplier<ProfilerFiller> supplier, boolean bl, boolean bl2, long l) {
        super(arg, arg2, arg3, supplier, bl, bl2, l);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(MinecraftServer minecraftServer, Executor executor, LevelStorageSource.LevelStorageAccess arg, ServerLevelData arg2, ResourceKey<Level> arg3, DimensionType arg4, ChunkProgressListener arg5, ChunkGenerator arg6, boolean bl, long l, List<CustomSpawner> list, boolean bl2, CallbackInfo ci) {
        this.players = new ObjectArrayList<>(this.players);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;noneMatch(Ljava/util/function/Predicate;)Z", remap = false))
    private boolean onTick(Stream<ServerPlayer> instance, Predicate<ServerPlayer> predicate) {
        for (ServerPlayer player : this.players) {
            if (!player.isSpectator() && !player.isSleepingLongEnough()) return false;
        }
        return true;
    }

    /**
     * @author Kasualix
     * @reason avoid stream and strange allocation
     */
    @Overwrite
    private void wakeUpAllPlayers() {
        for (ServerPlayer player : this.players) {
            if (player.isSleeping()) player.stopSleepInBed(false, false);
        }
    }
}

package com.teampotato.opotato.mixin.lazystronghold;

import com.teampotato.opotato.util.lazystronghold.ChunkGeneratorInterface;
import com.teampotato.opotato.util.lazystronghold.StrongholdGen;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IServerConfiguration;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Shadow @Final private Map<RegistryKey<World>, ServerWorld> levels;
    @Shadow private int tickCount;
    @Shadow @Final protected IServerConfiguration worldData;
    private boolean isNewWorld;

    @Inject(method = "stopServer", at = @At("HEAD"))
    private void stopStrongholdThreads(CallbackInfo ci) {
        this.levels.values().forEach(world -> {
            ChunkGenerator chunkGenerator = world.getChunkSource().getGenerator();
            StrongholdGen strongholdGen = ((ChunkGeneratorInterface) chunkGenerator).getStrongholdGen();
            if (strongholdGen == null) return;
            strongholdGen.stop();
        });
    }

    @Inject(method = "prepareLevels", at = @At("HEAD"))
    private void startStrongholdThreadIfNotNewWorld(CallbackInfo ci){
        if (this.isNewWorld) return;
        this.levels.values().forEach(world -> {
            StrongholdGen strongholdGen = ((ChunkGeneratorInterface) world.getChunkSource().generator).getStrongholdGen();
            if (strongholdGen == null || strongholdGen.started) return;
            strongholdGen.start();
        });
    }

    @Inject(method = "prepareLevels", at = @At("TAIL"))
    private void waitForStrongholdThreadIfNotNewWorld(CallbackInfo ci) {
        if (this.isNewWorld) return;
        this.levels.values().forEach(world -> {
            StrongholdGen strongholdGen = ((ChunkGeneratorInterface) world.getChunkSource().generator).getStrongholdGen();
            if (strongholdGen == null) return;
            if (!strongholdGen.started) strongholdGen.start();
            synchronized (strongholdGen.completedSignal) {
                while (!strongholdGen.completedSignal.get()) {
                    try {
                        strongholdGen.completedSignal.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Inject(method = "tickServer", at = @At("HEAD"))
    private void startStrongholdThread(CallbackInfo ci) {
        if (this.tickCount != 20) return;
        this.levels.values().forEach(world -> {
            StrongholdGen strongholdGen = ((ChunkGeneratorInterface) world.getChunkSource().generator).getStrongholdGen();
            if (strongholdGen == null || strongholdGen.started) return;
            strongholdGen.start();
        });
    }

    @Inject(method = "createLevels", at = @At("HEAD"))
    private void checkIfNewWorld(CallbackInfo ci) {
        this.isNewWorld = !this.worldData.overworldData().isInitialized();
    }
}

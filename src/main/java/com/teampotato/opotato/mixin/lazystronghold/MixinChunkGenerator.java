package com.teampotato.opotato.mixin.lazystronghold;

import com.teampotato.opotato.util.lazystronghold.ChunkGeneratorInterface;
import com.teampotato.opotato.util.lazystronghold.StrongholdGen;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSpreadSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

@Mixin(ChunkGenerator.class)
public class MixinChunkGenerator implements ChunkGeneratorInterface {
    private StrongholdGen strongholdGen;
    private static final double ROOT_2 = Math.sqrt(2);
    private static final int PADDING = 10;
    private final List<ChunkPos> strongholds = new CopyOnWriteArrayList<>();
    @Mutable @Shadow @Final private List<ChunkPos> strongholdPositions;
    @Shadow @Final private DimensionStructuresSettings settings;
    @Shadow @Final private long strongholdSeed;

    @Inject(method = "<init>(Lnet/minecraft/world/biome/provider/BiomeProvider;Lnet/minecraft/world/biome/provider/BiomeProvider;Lnet/minecraft/world/gen/settings/DimensionStructuresSettings;J)V", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        this.strongholdPositions = this.strongholds;
        StructureSpreadSettings stronghold = this.settings.stronghold();
        if (stronghold != null && stronghold.count() > 0) {
            this.strongholdGen = new StrongholdGen((ChunkGenerator) (Object) this, this.strongholdSeed, this.strongholdPositions);
        }
    }

    private int minSquaredDistance(){
        double d = 2.75 * Objects.requireNonNull(this.settings.stronghold()).distance() * 16 - 128 * ROOT_2;
        if(d <0) return 0;
        return (int) Math.pow((int) d >>4,2);
    }

    private int minSquaredDistanceWithPadding(){
        double d = 2.75 * Objects.requireNonNull(this.settings.stronghold()).distance() * 16 - 128 * ROOT_2;
        if(d - PADDING * 16 <0) return 0;
        return (int) Math.pow(((int) d >>4)-PADDING,2);
    }

    @Inject(method = "hasStronghold", at = @At("HEAD"))
    private void waitingForStrongholds(ChunkPos chunkPos, CallbackInfoReturnable<Boolean> cir) throws InterruptedException {
        if (this.strongholdGen == null) return;
        int squaredDistance = (chunkPos.x * chunkPos.x) + (chunkPos.z * chunkPos.z);
        if (squaredDistance < minSquaredDistanceWithPadding()) return;
        if (!strongholdGen.started) strongholdGen.start();
        if (squaredDistance < minSquaredDistance()) return;
        synchronized (strongholdGen.completedSignal) {
            while(!strongholdGen.completedSignal.get()) strongholdGen.completedSignal.wait();
        }
    }

    @Redirect(method = "findNearestMapFeature", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/ChunkGenerator;generateStrongholds()V"))
    private void waitForStrongholds2(ChunkGenerator instance) throws InterruptedException {
        if (this.strongholdGen == null) return;
        if(!strongholdGen.started)strongholdGen.start();
        synchronized (strongholdGen.completedSignal) {
            while(!strongholdGen.completedSignal.get()) strongholdGen.completedSignal.wait();
        }
    }

    @Redirect(method = "generateStrongholds", at = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/ChunkGenerator;biomeSource:Lnet/minecraft/world/biome/provider/BiomeProvider;"))
    private BiomeProvider getBiomeSource(ChunkGenerator instance) {
        return this.strongholdGen.biomeSource;
    }

    @Inject(method = "generateStrongholds", at = @At(value = "JUMP", ordinal = 6), cancellable = true)
    private void stopGenOnLeave(CallbackInfo ci) {
        if (this.strongholdGen == null) return;
        if (!this.strongholdGen.shouldStop) return;
        ci.cancel();
    }

    @Redirect(method = "hasStronghold", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/ChunkGenerator;generateStrongholds()V"))
    private void cancelStrongholdGen(ChunkGenerator instance) {}

    @Override
    public StrongholdGen getStrongholdGen() {
        return this.strongholdGen;
    }
}

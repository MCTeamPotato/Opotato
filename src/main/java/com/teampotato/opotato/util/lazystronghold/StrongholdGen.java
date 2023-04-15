package com.teampotato.opotato.util.lazystronghold;

import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.mixin.lazystronghold.ChunkGeneratorAccess;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.settings.StructureSpreadSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class StrongholdGen implements Runnable {
    public final StructureSpreadSettings config;
    private final Thread thread;
    private final ChunkGenerator generator;
    public final BiomeProvider biomeSource;
    private final long seed;
    public List<ChunkPos> strongholds;
    public boolean started;
    public final AtomicBoolean completedSignal;
    public boolean shouldStop;

    public StrongholdGen(ChunkGenerator generator, long seed, List<ChunkPos> strongholds) {
        this.started = false;
        this.shouldStop = false;
        this.completedSignal = new AtomicBoolean(false);
        this.seed = seed;
        this.biomeSource = ((ChunkGeneratorAccess) generator).getBiomeSource().withSeed(seed);
        this.thread = new Thread(this, "Stronghold thread");
        this.config = generator.getSettings().stronghold();
        this.strongholds = strongholds;
        this.generator = generator;
    }

    public void start() {
        this.started = true;
        this.thread.start();
    }

    public void stop() {
        this.shouldStop = true;
    }

    @Override
    public void run() {
        Opotato.LOGGER.info("Started stronghold gen thread");
        ((ChunkGeneratorAccess) this.generator).invokeGenerateStrongholds();
        if (this.shouldStop) {
            Opotato.LOGGER.info("Stronghold thread stopped early");
        } else {
            if (this.strongholds.size() != this.config.count()) {
                Opotato.LOGGER.error("Only " + this.strongholds.size() + " strongholds generated!");
            } else {
                Opotato.LOGGER.info("Generated " + this.config.count() + " strongholds.");
            }
        }
        synchronized (completedSignal) {
            completedSignal.set(true);
            completedSignal.notify();
        }
    }
}
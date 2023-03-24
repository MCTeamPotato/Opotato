package com.teampotato.opotato.mixin.opotato.rubidium;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.gl.device.RenderDevice;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.model.vertex.type.ChunkVertexType;
import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderBackend;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderManager;
import me.jellysquid.mods.sodium.client.render.chunk.backends.multidraw.MultidrawChunkRenderBackend;
import me.jellysquid.mods.sodium.client.render.chunk.backends.oneshot.ChunkRenderBackendOneshot;
import me.jellysquid.mods.sodium.client.render.chunk.format.DefaultModelVertexFormats;
import me.jellysquid.mods.sodium.client.world.ChunkStatusListener;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = SodiumWorldRenderer.class, remap = false)
public class MixinSodiumWorldRenderer implements ChunkStatusListener {

    @Shadow
    @Final
    private final LongSet loadedChunkPositions = new LongOpenHashSet();

    @Shadow
    private ChunkRenderManager<?> chunkRenderManager;

    @Shadow
    public void onChunkAdded(int x, int z) {
        this.loadedChunkPositions.add(ChunkPos.asLong(x, z));
        this.chunkRenderManager.onChunkAdded(x, z);
    }

    @Shadow
    public void onChunkRemoved(int x, int z) {
        this.loadedChunkPositions.remove(ChunkPos.asLong(x, z));
        this.chunkRenderManager.onChunkRemoved(x, z);
    }

    /**
     * @author Doctor Who
     * @reason Saving The World
     */
    @Overwrite
    private static ChunkRenderBackend<?> createChunkRenderBackend(RenderDevice device, SodiumGameOptions options, ChunkVertexType vertexFormat) {
        boolean disableBlacklist = SodiumClientMod.options().advanced.ignoreDriverBlacklist;
        return options.advanced.useChunkMultidraw && MultidrawChunkRenderBackend.isSupported(disableBlacklist) ? new MultidrawChunkRenderBackend(device, DefaultModelVertexFormats.MODEL_VERTEX_SFP) : new ChunkRenderBackendOneshot(DefaultModelVertexFormats.MODEL_VERTEX_SFP);
    }
}

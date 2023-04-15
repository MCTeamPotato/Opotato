package com.teampotato.opotato.mixin.lazystronghold;

import com.teampotato.opotato.util.lazystronghold.ChunkGeneratorInterface;
import com.teampotato.opotato.util.lazystronghold.StrongholdGen;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow @Nullable public abstract MinecraftServer getServer();

    @Inject(method = "setLevel", at = @At("HEAD"))
    private void startStrongholdGen(World world, CallbackInfo ci) {
        if (world.dimension() != World.OVERWORLD && world instanceof ServerWorld) {
            MinecraftServer server = this.getServer();
            if (server == null) return;
            server.getAllLevels().forEach(serverWorld -> {
                StrongholdGen strongholdGen = ((ChunkGeneratorInterface) serverWorld.getChunkSource().generator).getStrongholdGen();
                if (strongholdGen == null || strongholdGen.started) return;
                strongholdGen.start();
            });
        }
    }
}

package com.teampotato.opotato.mixin.opotato.witherstormmod;

import com.teampotato.opotato.util.opotato.MixinUtil;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.event.WitherStormChunkLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = WitherStormChunkLoader.class, remap = false)
public class MixinWitherStormChunkLoader {
    /**
     * @author Kasualix
     * @reason Optimize someone's shit
     */
    @Overwrite
    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.world.isClientSide) return;
        MixinUtil.WitherStormUtil.chunkWithWitherStormLoad((ServerWorld) event.world);
    }
}

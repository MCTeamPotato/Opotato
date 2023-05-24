package com.teampotato.opotato.mixin.opotato.ostoverhaul;

import glowsand.ostoverhaul.OstOverhaul;
import glowsand.ostoverhaul.ServerTickStuff;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ServerTickStuff.class, remap = false)
public abstract class MixinServerTickStuff {
    @Shadow
    public static int ticksorsmthn = 0;
    /**
     * @author Kasualix
     * @reason Optimize tick event
     */
    @Overwrite
    @SubscribeEvent
    public static void serverTickEnding(TickEvent.WorldTickEvent event){
        if (event.world instanceof ServerWorld || !event.phase.equals(TickEvent.Phase.END)) return;
        ticksorsmthn++;
        if (ticksorsmthn == 400) OstOverhaul.serverPlayerEntityStructureFeatureMap.clear();
    }
}

package com.teampotato.opotato.mixin.opotato.witherstormmod;

import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.event.WitherSicknessEvents;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Collection;

import static com.teampotato.opotato.util.opotato.MixinUtil.WitherStormUtil.*;

@Mixin(value = WitherSicknessEvents.class, remap = false)
public class MixinWitherSicknessEvents {
    /**
     * @author Kasualix
     * @reason Optimize someone's shit
     */
    @Overwrite
    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        World level = event.world;
        if (!(level instanceof ServerWorld) || !WitherStormModConfig.SERVER.witherSicknessEnabled.get() || event.phase != TickEvent.Phase.START) return;
        Collection<Entity> storms = ((ServerWorld) level).getEntities(WitherStormModEntityTypes.WITHER_STORM, entity -> entity instanceof WitherStormEntity && ((WitherStormEntity) entity).getPhase() > 1);
        if (PotatoCommonConfig.LET_WITHER_SICKNESS_ONLY_TAKE_EFFECT_ON_PLAYERS.get()) {
            playersHandle(level, storms);
        } else {
            allEntitiesHandle(level, storms);
        }
    }
}

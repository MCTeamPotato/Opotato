package com.teampotato.opotato.mixin.opotato.minecraft.ai;

import com.teampotato.opotato.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.YieldJobSite;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.Villager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(YieldJobSite.class)
public abstract class MixinYieldJobSite {
    @Shadow protected abstract void yieldJobSite(ServerLevel arg, Villager arg2, Villager arg3, BlockPos arg4, boolean bl);

    @Shadow protected abstract boolean nearbyWantsJobsite(PoiType arg, Villager arg2, BlockPos arg3);

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    protected void start(ServerLevel level, @NotNull Villager entity, long gameTime) {
        Optional<GlobalPos> memory = entity.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
        if (!memory.isPresent()) return;

        BlockPos jobSitePos = memory.get().pos();
        Optional<PoiType> optionalPoiType = level.getPoiManager().getType(jobSitePos);

        if (optionalPoiType.isPresent()) {
            for (Villager nearbyVillager : Utils.getNearbyVillagersWithCondition(entity, villager -> this.nearbyWantsJobsite(optionalPoiType.get(), villager, jobSitePos))) {
                this.yieldJobSite(level, entity, nearbyVillager, jobSitePos, nearbyVillager.getBrain().getMemory(MemoryModuleType.JOB_SITE).isPresent());
                break;
            }
        }
    }
}

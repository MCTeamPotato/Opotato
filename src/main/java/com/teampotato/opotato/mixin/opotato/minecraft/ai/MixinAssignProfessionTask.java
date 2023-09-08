package com.teampotato.opotato.mixin.opotato.minecraft.ai;

import com.teampotato.opotato.events.PotatoEvents;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.AssignProfessionFromJobSite;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Optional;

@Mixin(AssignProfessionFromJobSite.class)
public abstract class MixinAssignProfessionTask {
    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    protected void start(ServerLevel level, @NotNull Villager villager, long gameTime) {
        Optional<GlobalPos> memoryModuleType = villager.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
        if (!memoryModuleType.isPresent()) return;
        GlobalPos globalPos = memoryModuleType.get();
        villager.getBrain().eraseMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
        villager.getBrain().setMemory(MemoryModuleType.JOB_SITE, globalPos);
        level.broadcastEntityEvent(villager, (byte) 14);

        if (villager.getVillagerData().getProfession() == VillagerProfession.NONE) {
            ServerLevel poiLevel = level.getServer().getLevel(globalPos.dimension());
            if (poiLevel != null) {
                PoiType poiType = poiLevel.getPoiManager().getType(globalPos.pos()).orElse(null);

                if (poiType != null) {
                    for (VillagerProfession profession : PotatoEvents.VILLAGER_PROFESSIONS) {
                        if (profession.getJobPoiType() == poiType) {
                            villager.setVillagerData(villager.getVillagerData().setProfession(profession));
                            villager.refreshBrain(level);
                            break;
                        }
                    }
                }
            }
        }
    }
}

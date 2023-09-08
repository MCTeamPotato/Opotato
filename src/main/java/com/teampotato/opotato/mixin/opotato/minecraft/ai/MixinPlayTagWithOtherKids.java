package com.teampotato.opotato.mixin.opotato.minecraft.ai;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.PlayTagWithOtherKids;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Mixin(PlayTagWithOtherKids.class)
public abstract class MixinPlayTagWithOtherKids {

    @Shadow protected abstract boolean isFriendChasingMe(LivingEntity arg, LivingEntity arg2);

    @Unique
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    @Redirect(method = "checkExtraStartConditions(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/PathfinderMob;)Z", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", remap = false))
    private int onGetRandomValue(Random instance, int bound) {
        return random.nextInt(bound);
    }

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    @Nullable
    private LivingEntity seeIfSomeoneIsChasingMe(@NotNull LivingEntity livingEntity) {
        for (LivingEntity entity : livingEntity.getBrain().getMemory(MemoryModuleType.VISIBLE_VILLAGER_BABIES).orElse(Collections.emptyList())) {
            if (this.isFriendChasingMe(livingEntity, entity)) return entity;
        }

        return null;
    }

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    private @NotNull Map<LivingEntity, Integer> checkHowManyChasersEachFriendHas(@NotNull PathfinderMob pathfinderMob) {
        Map<LivingEntity, Integer> map = new Object2IntOpenHashMap<>();
        Optional<List<LivingEntity>> visibleVillagerBabies = pathfinderMob.getBrain().getMemory(MemoryModuleType.VISIBLE_VILLAGER_BABIES);
        if (!visibleVillagerBabies.isPresent()) return map;
        for (LivingEntity friend : visibleVillagerBabies.get()) friend.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).ifPresent(living -> map.compute(living, (livingEntity, integer) -> integer == null ? 1 : integer + 1));
        return map;
    }

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    private Optional<LivingEntity> findSomeoneToChase(@NotNull PathfinderMob pathfinderMob) {
        Optional<List<LivingEntity>> visibleVillagerBabies = pathfinderMob.getBrain().getMemory(MemoryModuleType.VISIBLE_VILLAGER_BABIES);
        if (!visibleVillagerBabies.isPresent()) return Optional.empty();
        List<LivingEntity> entities = visibleVillagerBabies.get();
        if (entities.isEmpty()) return Optional.empty();
        return Optional.of(entities.get(0));
    }

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    private Optional<LivingEntity> findSomeoneBeingChased(PathfinderMob pathfinderMob) {
        List<Map.Entry<LivingEntity, Integer>> friendChasers = new ObjectArrayList<>(this.checkHowManyChasersEachFriendHas(pathfinderMob).entrySet());
        friendChasers.sort(Comparator.comparingInt(Map.Entry::getValue));
        for (Map.Entry<LivingEntity, Integer> entry : friendChasers) {
            int chaserCount = entry.getValue();
            if (chaserCount > 0 && chaserCount <= 5) return Optional.of(entry.getKey());
        }

        return Optional.empty();
    }
}

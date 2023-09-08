package com.teampotato.opotato.mixin.opotato.minecraft.ai;

import com.teampotato.opotato.util.Utils;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(BehaviorUtils.class)
public abstract class MixinBehaviorUtils {
    /**
     * @author Kasualix
     * @reason switch to our method
     */
    @Overwrite
    public static Stream<Villager> getNearbyVillagersWithCondition(Villager villager, Predicate<Villager> villagerPredicate) {
        return Utils.getNearbyVillagersWithCondition(villager, villagerPredicate).stream();
    }
}

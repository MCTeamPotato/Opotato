package com.teampotato.opotato.mixin.opotato.minecraft.entity;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Mixin(BeehiveBlock.class)
public abstract class MixinBeehiveBlock {
    @Unique
    private static final Random random = ThreadLocalRandom.current();

    /**
     * @author Kasualix
     * @reason The {@link net.minecraft.world.level.Level#getEntitiesOfClass(Class, AABB)} method is very slow, tbh.
     */
    @Overwrite
    private void angerNearbyBees(@NotNull Level level, @NotNull BlockPos pos) {
        AABB aabb = new AABB((double) pos.getX() - 8.0, (double)pos.getY() - 6.0, (double)pos.getZ() - 8.0, (double)pos.getX() + 8.0, (double)pos.getY() + 6.0, (double)pos.getZ() + 8.0);
        List<Bee> bees = level.getEntitiesOfClass(Bee.class, aabb);
        if (!bees.isEmpty()) {
            List<Player> players = new ObjectArrayList<>();
            for (Player player : level.players()) {
                if (aabb.contains(player.position())) players.add(player);
            }

            if (!players.isEmpty()) {
                int size = players.size();
                for (Bee bee : bees) {
                    if (bee.getTarget() == null) bee.setTarget(players.get(random.nextInt(size)));
                }
            }
        }
    }
}

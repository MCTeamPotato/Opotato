package com.teampotato.opotato.mixin.opotato.minecraft;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Mixin(BeehiveBlock.class)
public abstract class MixinBeehiveBlock {
    /**
     * @author Kasualix
     * @reason The {@link net.minecraft.world.level.Level#getEntitiesOfClass(Class, AABB)} method is very slow, tbh.
     */
    @Overwrite
    private void angerNearbyBees(Level level, BlockPos pos) {
        AABB aabb = new AABB((double) pos.getX() - 8.0, (double)pos.getY() - 6.0, (double)pos.getZ() - 8.0, (double)pos.getX() + 8.0, (double)pos.getY() + 6.0, (double)pos.getZ() + 8.0);
        List<Bee> bees = level.getEntitiesOfClass(Bee.class, aabb);
        if (!bees.isEmpty()) {
            List<Player> players = new ObjectArrayList<>();
            for (Player player : level.players()) {
                if (aabb.contains(player.position())) players.add(player);
            }

            if (!players.isEmpty()) {
                int size = players.size();
                ThreadLocalRandom random = ThreadLocalRandom.current();
                for (Bee bee : bees) {
                    if (bee.getTarget() == null) bee.setTarget(players.get(random.nextInt(size)));
                }
            }
        }
    }
}

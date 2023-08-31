package com.teampotato.opotato.mixin.opotato.minecraft;

import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ChestBlockEntity.class)
public abstract class MixinChestBlockEntity {
    /**
     * @author Kasualix
     * @reason The {@link net.minecraft.world.level.Level#getEntitiesOfClass(Class, AABB)} method is very slow, tbh.
     */
    @Overwrite
    public static int getOpenCount(Level level, BaseContainerBlockEntity blockEntity, int j, int k, int l) {
        int i = 0;
        AABB searchBox = new AABB((double) j - 5.00, (double)k - 5.00, (double)l - 5.00, (double)(j + 1) + 5.00, (double)(k + 1) + 5.00, (double)(l + 1) + 5.00);

        for (Player player : level.players()) {
            if (player.containerMenu instanceof ChestMenu && searchBox.contains(player.position())) {
                Container container = ((ChestMenu)player.containerMenu).getContainer();

                if (container == blockEntity || (container instanceof CompoundContainer && ((CompoundContainer)container).contains(blockEntity))) {
                    i++;
                }
            }
        }

        return i;
    }

}

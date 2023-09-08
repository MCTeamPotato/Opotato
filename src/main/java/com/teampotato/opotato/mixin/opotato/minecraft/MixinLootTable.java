package com.teampotato.opotato.mixin.opotato.minecraft;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(LootTable.class)
public abstract class MixinLootTable {
    @Shadow @Final private List<LootPool> pools;

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    public LootPool getPool(String name) {
        for (LootPool pool : this.pools) {
            if (name.equals(pool.getName())) return pool;
        }
        return null;
    }
}

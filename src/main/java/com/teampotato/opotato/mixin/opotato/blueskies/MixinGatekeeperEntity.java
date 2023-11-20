package com.teampotato.opotato.mixin.opotato.blueskies;

import com.legacy.blue_skies.entities.villager.GatekeeperEntity;
import com.teampotato.opotato.config.mods.BlueSkiesExtraConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GatekeeperEntity.class)
public abstract class MixinGatekeeperEntity {
    @Unique
    private static ItemLike tradeItem_cache = null;

    @ModifyArg(method = "getTradesForProgression", at = @At(value = "INVOKE", ordinal = 0, target = "Lcom/legacy/blue_skies/entities/villager/SkiesVillagerTrades$SingleTrade$Builder;item(Lnet/minecraft/world/level/ItemLike;I)Lcom/legacy/blue_skies/entities/villager/SkiesVillagerTrades$SingleTrade$Builder;"))
    private ItemLike modifyEmerald(ItemLike item) {
        if (tradeItem_cache == null) tradeItem_cache = ForgeRegistries.ITEMS.getValue(new ResourceLocation(BlueSkiesExtraConfig.itemToTradeZealLighter.get()));
        return tradeItem_cache;
    }
}

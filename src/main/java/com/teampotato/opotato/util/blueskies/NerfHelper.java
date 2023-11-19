package com.teampotato.opotato.util.blueskies;

import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.items.SkyArmorItem;
import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.blue_skies.util.EntityUtil;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class NerfHelper {
    /**
     * Rather expensive on performance, so separated from the mixin to better monitor this in Spark
     **/
    public static void enhancedNerfInBlueSkiesDims(Player player, int returnedArmorValue, CallbackInfoReturnable<Integer> cir, ResourceKey<Level> dimension) {
        if (dimension == SkiesDimensions.everbrightKey() || dimension == SkiesDimensions.everdawnKey()) {
            SkiesPlayer.ifPresent(player, (skyPlayer) -> {
                if (!EntityUtil.hasPlayerCompletedProgression(skyPlayer)) {
                    int i = 0;
                    for (ItemStack stack : player.getArmorSlots()) {
                        Item item = stack.getItem();
                        if (item instanceof SkyArmorItem) continue;
                        if (item instanceof ArmorItem) {
                            ArmorItem armorItem = (ArmorItem) item;
                            i += armorItem.getMaterial().getDefenseForSlot(armorItem.getSlot());
                        }
                    }

                    if (i <= returnedArmorValue) cir.setReturnValue(returnedArmorValue - i);
                }
            });
        }
    }
}

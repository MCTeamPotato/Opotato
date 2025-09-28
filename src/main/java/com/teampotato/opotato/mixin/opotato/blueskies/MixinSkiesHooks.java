package com.teampotato.opotato.mixin.opotato.blueskies;

import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.events.SkiesHooks;
import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.blue_skies.util.EntityUtil;
import com.teampotato.opotato.config.mods.BlueSkiesExtraConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

@Mixin(value = SkiesHooks.class, remap = false)
public class MixinSkiesHooks {
    /**
     * @author Kall
     * @reason F**k this sh*t
     */
    @Overwrite
    public static float breakSpeedHook(float speed, BlockState state, @Nullable BlockPos pos, Player player) {
        if (!BlueSkiesExtraConfig.enableBreakNerf.get()) return speed;
        Block block = state.getBlock();
        Item item = player.getMainHandItem().getItem();
        if (item != null && pos != null) {
            if (SkiesHooks.webHarvestCheck(block, item)) {
                return 15.0F;
            }

            if (!Objects.requireNonNull(item.getRegistryName()).getNamespace().equals("blue_skies") && item instanceof DiggerItem && SkiesDimensions.inSkyDimension(player) && (SkiesHooks.isApplicableTool((DiggerItem)item, state) || player.getMainHandItem().getToolTypes().contains(state.getHarvestTool()))) {
                return Optional.ofNullable(SkiesPlayer.getIfPresent(player, (skyPlayer) -> {
                    if (!EntityUtil.hasPlayerCompletedProgression(skyPlayer)) {
                        if (BlueSkiesExtraConfig.requireProgressionForToolUsage.get()) {
                            player.displayClientMessage(new TranslatableComponent("gui.blue_skies.tooltip.invalid_tool"), true);
                            return 0.999F;
                        } else {
                            return speed;
                        }
                    } else {
                        return speed;
                    }
                })).orElseThrow(RuntimeException::new);
            }
        }

        return speed;
    }
}

package com.teampotato.opotato.mixin.opotato.blueskies;

import com.legacy.blue_skies.blocks.misc.SkyWebbingBlock;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.events.SkiesHooks;
import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.util.EntityUtil;
import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WebBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.Objects;

@Mixin(value = SkiesHooks.class, remap = false)
public class MixinSkiesHooks {
    /**
     * @author Doctor Who
     * @reason Saving The World
     */
    @Overwrite
    public static float breakSpeedHook(float speed, BlockState state, @Nullable BlockPos pos, PlayerEntity player) {
        Block block = state.getBlock();
        Item item = player.getMainHandItem().getItem();
        if (pos != null) {
            if (webHarvestCheck(block, item)) {
                return 15.0F;
            }

            if (PotatoCommonConfig.ENABLE_BLUE_SKIES_NERF.get() && !Objects.requireNonNull(item.getRegistryName()).getNamespace().equals("blue_skies") && item instanceof ToolItem && SkiesDimensions.inSkyDimension(player) && (isApplicableTool((ToolItem)item, state) || player.getMainHandItem().getToolTypes().contains(state.getHarvestTool()))) {
                return Objects.requireNonNullElse(SkiesPlayer.getIfPresent(player, (skyPlayer) -> {
                    if (!EntityUtil.hasPlayerCompletedProgression(skyPlayer)) {
                        player.displayClientMessage(new TranslationTextComponent("gui.blue_skies.tooltip.invalid_tool"), true);
                        return 0.999F;
                    } else {
                        return speed;
                    }
                }), speed);
            }
        }

        return speed;
    }

    @Shadow
    public static boolean isApplicableTool(ToolItem item, BlockState state) {
        return item.getTier() != ItemTier.WOOD && item.getTier() != ItemTier.STONE && (state.getMaterial() == Material.STONE && item instanceof PickaxeItem || state.getMaterial() == Material.WOOD && item instanceof AxeItem || (state.getMaterial() == Material.SAND || state.getMaterial() == Material.GRASS || state.getMaterial() == Material.DIRT) && item instanceof ShovelItem);
    }

    @Shadow
    public static boolean webHarvestCheck(Block block, Item item) {
        return block instanceof WebBlock && item == SkiesItems.ventium_shears || block instanceof SkyWebbingBlock && item.isCorrectToolForDrops(Blocks.COBWEB.defaultBlockState());
    }
}

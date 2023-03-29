package com.teampotato.opotato.mixin.opotato.blueskies;

import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.entities.util.SkiesEntityHooks;
import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.blue_skies.util.EntityUtil;
import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;

@Mixin(value = SkiesEntityHooks.class, remap = false)
public class MixinSkiesEntityHooks {

    /**
     * @author Doctor Who
     * @reason Saving The World
     */
    @Overwrite
    public static float nerfDamage(DamageSource source, float amount) {
        if (source.getDirectEntity() instanceof PlayerEntity && SkiesDimensions.inSkyDimension(source.getDirectEntity()) && PotatoCommonConfig.ENABLE_BLUE_SKIES_NERF.get()) {
            PlayerEntity player = (PlayerEntity)source.getDirectEntity();
            ItemStack weapon = player.getMainHandItem();
            Float skiesPlayer = SkiesPlayer.getIfPresent(player, (skyPlayer) -> {
                if (!EntityUtil.hasPlayerCompletedProgression(skyPlayer) && !Objects.requireNonNull(weapon.getItem().getRegistryName()).getNamespace().equals("blue_skies") && !(player.getAttributeValue(Attributes.ATTACK_DAMAGE) <= 1.0)) {
                    player.displayClientMessage(new TranslationTextComponent("gui.blue_skies.tooltip.invalid_weapon"), true);
                    return amount * 0.3F;
                } else {
                    return amount;
                }
            });
            return Objects.requireNonNullElse(skiesPlayer, amount);
        } else {
            return amount;
        }
    }

    /**
     * @author Doctor Who
     * @reason Saving The World
     */
    @Overwrite
    public static float nerfIndirectDamage(DamageSource source, float amount) {
        if (source.getEntity() != null && source.getEntity() instanceof PlayerEntity && SkiesDimensions.inSkyDimension(source.getEntity()) && PotatoCommonConfig.ENABLE_BLUE_SKIES_NERF.get()) {
            PlayerEntity player = (PlayerEntity)source.getEntity();
            ResourceLocation name = Objects.requireNonNull(source.getDirectEntity()).getType().getRegistryName();
            return Objects.requireNonNullElse(SkiesPlayer.getIfPresent(player, (skyPlayer) -> EntityUtil.hasPlayerCompletedProgression(skyPlayer) || name != null && name.getNamespace().equals("blue_skies") ? amount : Math.min(5.0F, amount)), Math.min(5.0F, amount));
        } else {
            return Math.min(5.0F, amount);
        }
    }
}

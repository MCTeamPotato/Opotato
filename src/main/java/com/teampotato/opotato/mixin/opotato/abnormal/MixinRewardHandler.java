package com.teampotato.opotato.mixin.opotato.abnormal;

import com.minecraftabnormals.abnormals_core.client.RewardHandler;
import com.minecraftabnormals.abnormals_core.client.model.SlabfishHatModel;
import com.minecraftabnormals.abnormals_core.client.renderer.SlabfishHatLayerRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = RewardHandler.class, remap = false)
public abstract class MixinRewardHandler {
    /**
     * @author Kasualix
     * @reason remove Internet connection behavior
     */
    @Overwrite
    public static void clientSetup(FMLClientSetupEvent event) {
        Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().values().forEach(renderer -> renderer.addLayer(new SlabfishHatLayerRenderer(renderer, new SlabfishHatModel())));
    }
}

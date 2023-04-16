package com.teampotato.opotato.mixin.opotato.undergarden;

import com.teampotato.opotato.config.PotatoCommonConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import quek.undergarden.registry.UGStructures;

@Mixin(value = UGStructures.class, remap = false)
public class MixinUGStructures {
    @Shadow private static <F extends Structure<?>> void setupStructure(F structure, StructureSeparationSettings structureSeparationSettings, boolean transformSurroundingLand) {}
    @Shadow @Final public static RegistryObject<Structure<NoFeatureConfig>> CATACOMBS;

    /**
     * @author Kasualix
     * @reason Allow structure generation configurations
     */
    @Overwrite
    public static void registerStructures() {
        setupStructure(CATACOMBS.get(), new StructureSeparationSettings(PotatoCommonConfig.CATACOMB_SPACING.get(), PotatoCommonConfig.CATACOMB_SEPARATION.get(), 276320045), true);
    }
}

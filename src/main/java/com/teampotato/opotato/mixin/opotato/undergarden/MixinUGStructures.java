package com.teampotato.opotato.mixin.opotato.undergarden;

import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.fml.RegistryObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import quek.undergarden.registry.UGStructures;

import static com.teampotato.opotato.config.mods.UndergardenExtraConfig.*;

@Mixin(UGStructures.class)
public abstract class MixinUGStructures {
    @Shadow
    private static  <F extends StructureFeature<?>> void setupStructure(F structure, StructureFeatureConfiguration structureSeparationSettings, boolean transformSurroundingLand) {
        throw new RuntimeException();
    }

    @Shadow(remap = false) @Final public static RegistryObject<StructureFeature<NoneFeatureConfiguration>> CATACOMBS;

    /**
     * @author Kasualix
     * @reason impl config
     */
    @Overwrite(remap = false)
    public static void registerStructures() {
        setupStructure(CATACOMBS.get(), new StructureFeatureConfiguration(catacombSpacing.get(), catacombSeparation.get(), catacombSalt.get()), catacombTransformSurroundingLand.get());
    }
}

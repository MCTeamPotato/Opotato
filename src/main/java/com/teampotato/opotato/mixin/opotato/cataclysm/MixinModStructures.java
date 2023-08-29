package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.init.ModStructures;
import com.teampotato.opotato.config.CataclysmExtraConfig;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.fml.RegistryObject;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ModStructures.class)
public abstract class MixinModStructures {
    @Shadow public static  <F extends StructureFeature<?>> void setupMapSpacingAndLand(F structure, StructureFeatureConfiguration structureSeparationSettings, boolean transformSurroundingLand) {}
    @Shadow(remap = false) @Final public static RegistryObject<StructureFeature<NoneFeatureConfiguration>> SOUL_BLACK_SMITH;
    @Shadow(remap = false) @Final public static RegistryObject<StructureFeature<NoneFeatureConfiguration>> RUINED_CITADEL;
    @Shadow(remap = false) @Final public static RegistryObject<StructureFeature<NoneFeatureConfiguration>> BURNING_ARENA;

    /**
     * @author Kasualix
     * @reason impl gen config
     */
    @Overwrite(remap = false)
    public static void setupStructures() {
        setupMapSpacingAndLand(SOUL_BLACK_SMITH.get(), new StructureFeatureConfiguration(CataclysmExtraConfig.soulBlackSmithSpacing.get(), CataclysmExtraConfig.soulBlackSmithSeparation.get(), CataclysmExtraConfig.soulBlackSmithSalt.get().intValue()), CataclysmExtraConfig.soulBlackSmithTransformSurroundingLand.get());
        setupMapSpacingAndLand(RUINED_CITADEL.get(), new StructureFeatureConfiguration(CataclysmExtraConfig.ruinedCitadelSpacing.get(), CataclysmExtraConfig.ruinedCitadelSeparation.get(), CataclysmExtraConfig.ruinedCitadelSalt.get().intValue()), CataclysmExtraConfig.ruinedCitadelTransformSurroundingLand.get());
        setupMapSpacingAndLand(BURNING_ARENA.get(), new StructureFeatureConfiguration(CataclysmExtraConfig.burningArenaSpacing.get(), CataclysmExtraConfig.burningArenaSeparation.get(), CataclysmExtraConfig.burningArenaSalt.get().intValue()), CataclysmExtraConfig.burningArenaTransformSurroundingLand.get());
    }

    @Redirect(method = "addDimensionalSpacing", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;)V", remap = false), remap = false)
    private static void onLog(Logger instance, String s) {}
}

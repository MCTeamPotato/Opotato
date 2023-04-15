package com.teampotato.opotato.mixin.opotato.cataclysm;

import L_Ender.cataclysm.init.ModStructures;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.teampotato.opotato.config.PotatoCommonConfig.*;

@Mixin(value = ModStructures.class, remap = false)
public abstract class MixinModStructures {
    @Shadow
    public static <F extends Structure<?>> void setupMapSpacingAndLand(F structure, StructureSeparationSettings structureSeparationSettings, boolean transformSurroundingLand) {}

    @Shadow @Final public static RegistryObject<Structure<NoFeatureConfig>> SOUL_BLACK_SMITH;
    @Shadow @Final public static RegistryObject<Structure<NoFeatureConfig>> RUINED_CITADEL;
    @Shadow @Final public static RegistryObject<Structure<NoFeatureConfig>> BURNING_ARENA;

    @Inject(method = "setupStructures", at = @At("HEAD"), cancellable = true)
    private static void on_setupStructures(CallbackInfo ci) {
        setupMapSpacingAndLand(SOUL_BLACK_SMITH.get(), new StructureSeparationSettings(SOUL_BLACK_SMITH_SPACING.get(), SOUL_BLACK_SMITH_SEPARATION.get(), 1984567320), false);
        setupMapSpacingAndLand(RUINED_CITADEL.get(), new StructureSeparationSettings(RUINED_CITADEL_SPACING.get(), RUINED_CITADEL_SEPARATION.get(), 367895146), false);
        setupMapSpacingAndLand(BURNING_ARENA.get(), new StructureSeparationSettings(BURNING_ARENA_SPACING.get(), BURNING_ARENA_SEPARATION.get(), 1923456789), false);
        ci.cancel();
    }
}

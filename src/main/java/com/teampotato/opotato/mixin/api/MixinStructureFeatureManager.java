package com.teampotato.opotato.mixin.api;

import com.teampotato.opotato.api.IStructureFeatureManager;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.chunk.FeatureAccess;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StructureFeatureManager.class)
public class MixinStructureFeatureManager implements IStructureFeatureManager {

    @Override
    public StructureStart<?> getStartForFeature(StructureFeature<?> structure, FeatureAccess reader) {
        return reader.getStartForFeature(structure);
    }
}

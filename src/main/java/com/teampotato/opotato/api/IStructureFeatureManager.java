package com.teampotato.opotato.api;

import net.minecraft.world.level.chunk.FeatureAccess;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureStart;

public interface IStructureFeatureManager {
    StructureStart<?> getStartForFeature(StructureFeature<?> structure, FeatureAccess reader);
}

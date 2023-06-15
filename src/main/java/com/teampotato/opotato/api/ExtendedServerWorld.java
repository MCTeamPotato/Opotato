package com.teampotato.opotato.api;

import net.minecraft.entity.Entity;

import java.util.UUID;

public interface ExtendedServerWorld {
    Entity findAddedOrPendingEntityPublic(UUID uuid);
}

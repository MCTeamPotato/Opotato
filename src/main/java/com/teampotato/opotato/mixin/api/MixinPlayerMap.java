package com.teampotato.opotato.mixin.api;

import com.teampotato.opotato.api.IPlayerMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.server.level.PlayerMap;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(PlayerMap.class)
public class MixinPlayerMap implements IPlayerMap {
    @Shadow @Final private Object2BooleanMap<ServerPlayer> players;

    @Override
    public Set<ServerPlayer> getPlayerSet() {
        return this.players.keySet();
    }
}

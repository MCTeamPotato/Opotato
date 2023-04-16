package com.teampotato.opotato.mixin.opotato.ostoverhaul;

import glowsand.ostoverhaul.OstOverhaul;
import glowsand.ostoverhaul.ServerTickStuff;
import glowsand.ostoverhaul.StructureMessage;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(value = ServerTickStuff.class, remap = false)
public class MixinServerTickStuff {
    @Shadow
    public static int ticksorsmthn = 0;
    /**
     * @author Kasualix
     * @reason Optimize tick event
     */
    @Overwrite
    @SubscribeEvent
    public static void serverTickEnding(TickEvent.WorldTickEvent event){
        if (event.world.isClientSide || !event.phase.equals(TickEvent.Phase.END)) return;
        ticksorsmthn++;
        if (ticksorsmthn % 20 == 0) {
            List<ServerPlayerEntity> playerList = Objects.requireNonNull(event.world.getServer()).getPlayerList().getPlayers();
            AtomicInteger index = new AtomicInteger();
            if (playerList.stream().anyMatch(playerEntity -> {
                if (LocationPredicate.inFeature(Structure.STRONGHOLD).matches(playerEntity.getLevel(), playerEntity.getX(), playerEntity.getY(), playerEntity.getZ()) && !OstOverhaul.serverPlayerEntityStructureFeatureMap.getOrDefault(playerEntity, Structure.BASTION_REMNANT).equals(Structure.STRONGHOLD)) return indexSetter(index, playerList, playerEntity);
                return false;
            })) {
                OstOverhaul.serverPlayerEntityStructureFeatureMap.put(playerList.get(index.get()), Structure.STRONGHOLD);
                OstOverhaul.INSTANCE.send(PacketDistributor.PLAYER.with(()->playerList.get(index.get())), new StructureMessage(1));
            } else if (playerList.stream().anyMatch(playerEntity -> {
                if (LocationPredicate.inFeature(Structure.BASTION_REMNANT).matches(playerEntity.getLevel(), playerEntity.getX(), playerEntity.getY(), playerEntity.getZ()) && !OstOverhaul.serverPlayerEntityStructureFeatureMap.getOrDefault(playerEntity, Structure.STRONGHOLD).equals(Structure.BASTION_REMNANT)) return indexSetter(index, playerList, playerEntity);
                return false;
            })) {
                OstOverhaul.serverPlayerEntityStructureFeatureMap.put(playerList.get(index.get()), Structure.BASTION_REMNANT);
                OstOverhaul.INSTANCE.send(PacketDistributor.PLAYER.with(()->playerList.get(index.get())),new StructureMessage(2));
            } else {
                if (playerList.stream().anyMatch(playerEntity -> {
                    if (OstOverhaul.serverPlayerEntityStructureFeatureMap.getOrDefault(playerEntity, null) != null) return indexSetter(index, playerList, playerEntity);
                    return false;
                })) {
                    OstOverhaul.serverPlayerEntityStructureFeatureMap.remove(playerList.get(index.get()));
                    OstOverhaul.INSTANCE.send(PacketDistributor.PLAYER.with(()->playerList.get(index.get())),new StructureMessage(0));
                }
            }
        }
        if (ticksorsmthn == 400) OstOverhaul.serverPlayerEntityStructureFeatureMap.clear();
    }

    private static boolean indexSetter(AtomicInteger index, List<ServerPlayerEntity> list, ServerPlayerEntity player) {
        index.set(list.indexOf(player));
        return true;
    }
}

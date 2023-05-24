package com.teampotato.opotato.event;

import glowsand.ostoverhaul.OstOverhaul;
import glowsand.ostoverhaul.ServerTickStuff;
import glowsand.ostoverhaul.StructureMessage;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class OstEvents {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (ServerTickStuff.ticksorsmthn % 20 != 0 || !(event.player.level instanceof ServerWorld) || !(event.player instanceof ServerPlayerEntity))  return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.player;
        ServerWorld world = (ServerWorld) event.player.level;
        if (LocationPredicate.inFeature(Structure.STRONGHOLD).matches(world, player.getX(), player.getY(), player.getZ()) && !OstOverhaul.serverPlayerEntityStructureFeatureMap.getOrDefault(player, Structure.BASTION_REMNANT).equals(Structure.STRONGHOLD)) {
            OstOverhaul.serverPlayerEntityStructureFeatureMap.put(player, Structure.STRONGHOLD);
            OstOverhaul.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new StructureMessage(1));
        } else if (LocationPredicate.inFeature(Structure.BASTION_REMNANT).matches(world, player.getX(), player.getY(), player.getZ()) && !OstOverhaul.serverPlayerEntityStructureFeatureMap.getOrDefault(player, Structure.STRONGHOLD).equals(Structure.BASTION_REMNANT)) {
            OstOverhaul.serverPlayerEntityStructureFeatureMap.put(player, Structure.BASTION_REMNANT);
            OstOverhaul.INSTANCE.send(PacketDistributor.PLAYER.with(()-> player),new StructureMessage(2));
        } else if (OstOverhaul.serverPlayerEntityStructureFeatureMap.getOrDefault(player, null) != null) {
            OstOverhaul.serverPlayerEntityStructureFeatureMap.remove(player);
            OstOverhaul.INSTANCE.send(PacketDistributor.PLAYER.with(()-> player),new StructureMessage(0));
        }
    }
}

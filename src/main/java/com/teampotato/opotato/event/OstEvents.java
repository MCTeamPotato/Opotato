package com.teampotato.opotato.event;

import glowsand.ostoverhaul.ServerTickStuff;
import glowsand.ostoverhaul.StructureMessage;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import static glowsand.ostoverhaul.OstOverhaul.INSTANCE;
import static glowsand.ostoverhaul.OstOverhaul.serverPlayerEntityStructureFeatureMap;

public class OstEvents {
    private static final LocationPredicate strongholdPredicate = LocationPredicate.inFeature(Structure.STRONGHOLD);
    private static final LocationPredicate bastionRemnantPredicate = LocationPredicate.inFeature(Structure.BASTION_REMNANT);

    private static final StructureMessage message0 = new StructureMessage(0);
    private static final StructureMessage message1 = new StructureMessage(1);
    private static final StructureMessage message2 = new StructureMessage(2);

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (ServerTickStuff.ticksorsmthn % 20 != 0 || !(event.player.level instanceof ServerWorld) || !(event.player instanceof ServerPlayerEntity))  return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.player;
        ServerWorld world = (ServerWorld) event.player.level;
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        if (
                strongholdPredicate.matches(world, x, y, z) &&
                !serverPlayerEntityStructureFeatureMap.getOrDefault(player, Structure.BASTION_REMNANT).equals(Structure.STRONGHOLD)
        ) {
            serverPlayerEntityStructureFeatureMap.put(player, Structure.STRONGHOLD);
            INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message1);
        } else if (
                bastionRemnantPredicate.matches(world, x, y, z) &&
                !serverPlayerEntityStructureFeatureMap.getOrDefault(player, Structure.STRONGHOLD).equals(Structure.BASTION_REMNANT)
        ) {
            serverPlayerEntityStructureFeatureMap.put(player, Structure.BASTION_REMNANT);
            INSTANCE.send(PacketDistributor.PLAYER.with(()-> player), message2);
        } else if (serverPlayerEntityStructureFeatureMap.getOrDefault(player, null) != null) {
            serverPlayerEntityStructureFeatureMap.remove(player);
            INSTANCE.send(PacketDistributor.PLAYER.with(()-> player), message0);
        }
    }
}
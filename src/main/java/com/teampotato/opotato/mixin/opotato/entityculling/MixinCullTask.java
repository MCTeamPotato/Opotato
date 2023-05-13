package com.teampotato.opotato.mixin.opotato.entityculling;

import com.google.common.collect.Lists;
import dev.tr7zw.entityculling.CullTask;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Set;

@Mixin(value = CullTask.class, remap = false)
public abstract class MixinCullTask {
    private List<? extends String> entityWhitelist = Lists.newArrayList(
            "minecraft:ender_dragon", "minecraft:ghast", "minecraft:wither",
            "alexsmobs:void_worm", "alexsmobs:void_worm_part", "alexsmobs:spectre",
            "twilightforest:naga", "twilightforest:lich", "twilightforest:yeti", "twilightforest:snow_queen", "twilightforest:minoshroom", "twilightforest:hydra", "twilightforest:knight_phantom", "twilightforest:ur_ghast",
            "atum:pharaoh",
            "mowziesmobs:barako", "mowziesmobs:ferrous_wroughtnaut", "mowziesmobs:frostmaw", "mowziesmobs:naga",
            "aoa3:skeletron", "aoa3:smash", "aoa3:baroness", "aoa3:clunkhead", "aoa3:corallus", "aoa3:cotton_candor", "aoa3:craexxeus", "aoa3:xxeus", "aoa3:creep", "aoa3:crystocore", "aoa3:dracyon", "aoa3:graw", "aoa3:gyro", "aoa3:hive_king", "aoa3:kajaros", "aoa3:miskel", "aoa3:harkos", "aoa3:raxxan", "aoa3:okazor", "aoa3:king_bambambam", "aoa3:king_shroomus", "aoa3:kror", "aoa3:mechbot", "aoa3:nethengeic_wither", "aoa3:red_guardian", "aoa3:blue_guardian", "aoa3:green_guardian", "aoa3:yellow_guardian", "aoa3:rock_rider", "aoa3:shadowlord", "aoa3:tyrosaur", "aoa3:vinecorne", "aoa3:visualent", "aoa3:voxxulon", "aoa3:bane", "aoa3:elusive",
            "gaiadimension:malachite_drone", "gaiadimension:malachite_guard",
            "blue_skies:alchemist", "blue_skies:arachnarch", "blue_skies:starlit_crusher", "blue_skies:summoner",
            "stalwart_dungeons:awful_ghast", "stalwart_dungeons:nether_keeper", "stalwart_dungeons:shelterer_without_armor",
            "dungeonsmod:extrapart", "dungeonsmod:king", "dungeonsmod:deserted", "dungeonsmod:crawler", "dungeonsmod:ironslime", "dungeonsmod:kraken", "dungeonsmod:voidmaster", "dungeonsmod:lordskeleton", "dungeonsmod:winterhunter", "dungeonsmod:sun",
            "forestcraft:beequeen", "forestcraft:iguana_king", "forestcraft:cosmic_fiend", "forestcraft:nether_scourge",
            "cataclysm:ender_golem", "cataclysm:ender_guardian", "cataclysm:ignis", "cataclysm:ignited_revenant", "cataclysm:netherite_monstrosity",
            "iceandfire:fire_dragon", "iceandfire:ice_dragon", "iceandfire:lightning_dragon", "iceandfire:dragon_multipart"
    );

    private List<? extends String> entityModidList = Lists.newArrayList(
            "create", "witherstormmod"
    );

    @Redirect(method = "run", at = @At(value = "INVOKE", target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z"))
    private boolean onCheckBlackList(Set set, Object o) {
        if (o instanceof TileEntityType) {
            TileEntityType tileEntityType = (TileEntityType) o;
            return (tileEntityType.getRegistryName() == null || set.contains(o) || tileEntityType.getRegistryName().getNamespace().equals("create"));
        } else {
            EntityType entityType = (EntityType) o;
            return (entityType.getRegistryName() == null || set.contains(o) || entityType.getRegistryName().getPath().equals("image_frame") || entityWhitelist.contains(entityType.getRegistryName().toString()) || entityModidList.contains(entityType.getRegistryName().getNamespace()));
        }
    }
}

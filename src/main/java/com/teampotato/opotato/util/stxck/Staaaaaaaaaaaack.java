package com.teampotato.opotato.util.stxck;


import com.teampotato.opotato.config.mods.stxck.StxckClientConfig;
import com.teampotato.opotato.config.mods.stxck.StxckCommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Staaaaaaaaaaaack {
    public static final String MODID = "staaaaaaaaaaaack";

    public static final Tag.Named<Item> BLACK_LIST_TAG = ItemTags.createOptional(new ResourceLocation(MODID + ":blacklist"));
    private static Set<Item> itemBlackList;

    public static StxckCommonConfig commonConfig;
    public static StxckClientConfig clientConfig;

    public static Function<ResourceLocation, Item> registriesFetcher;


    public static Set<Item> getItemBlackList() {
        if (itemBlackList == null) {
            itemBlackList = commonConfig.getItemBlackList().stream()
                    .map(ResourceLocation::new)
                    .map(registriesFetcher)
                    .collect(Collectors.toSet());
        }
        return itemBlackList;
    }
}

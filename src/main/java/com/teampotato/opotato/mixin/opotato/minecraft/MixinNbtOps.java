package com.teampotato.opotato.mixin.opotato.minecraft;

import com.mojang.serialization.DataResult;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(NbtOps.class)
public abstract class MixinNbtOps {
    @Shadow
    private static CollectionTag<?> createGenericList(byte collectionElementId, byte newElementId) {
        throw new RuntimeException();
    }
    @Shadow
    private static <T extends Tag> void fillMany(CollectionTag<T> collectionTag, Tag mergingCollectionTag, List<Tag> elementTags) {
        throw new RuntimeException();
    }

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    public DataResult<Tag> mergeToList(Tag tag, List<Tag> tagList) {
        if (!(tag instanceof CollectionTag) && !(tag instanceof EndTag)) {
            return DataResult.error("mergeToList called with not a list: " + tag, tag);
        } else {
            byte elementType = (byte)0;
            byte listId = (byte)0;

            if (tag instanceof CollectionTag) elementType = ((CollectionTag<?>) tag).getElementType();
            if (!tagList.isEmpty()) listId = tagList.get(0).getId();

            CollectionTag<?> collectionTag = createGenericList(elementType, listId);
            fillMany(collectionTag, tag, tagList);
            return DataResult.success(collectionTag);
        }
    }
}

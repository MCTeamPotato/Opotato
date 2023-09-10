package com.teampotato.opotato.util.stxck;

import com.teampotato.opotato.mixin.stxck.ItemEntityAccessor;
import com.teampotato.opotato.mixin.stxck.ItemStackAccessor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

public class StxckUtil {
    public static final String EXTRA_ITEM_COUNT_TAG = "StxckExtraItemCount";
    private static EntityDataAccessor<Integer> DATA_EXTRA_ITEM_COUNT;

    public static BiPredicate<ItemStack, ItemStack> areMergableReplacementPredicate;


    public static void setDataExtraItemCount(EntityDataAccessor<Integer> entityDataAccessor) {
        if (DATA_EXTRA_ITEM_COUNT != null) return;
        DATA_EXTRA_ITEM_COUNT = entityDataAccessor;
    }

    @SuppressWarnings("deprecation")
    public static void refillItemStack(ItemEntity entity) {
        int extraItemCount = getExtraItemCount(entity);
        if (extraItemCount <= 0) return ;

        ItemStack stack = entity.getItem();
        Optional.ofNullable(((ItemStackAccessor) (Object) stack).accessItem())
                .map(Item::getMaxStackSize)
                .ifPresent(maxSize -> {
                    if (stack.getCount() == maxSize) return;
                    int x = maxSize - stack.getCount();
                    int refillCount = Math.min(x, extraItemCount);
                    stack.grow(refillCount);
                    setExtraItemCount(entity, extraItemCount - refillCount);
                    entity.setItem(stack.copy());
                });
    }

    public static boolean areMergable(ItemEntity itemEntity, ItemEntity itemEntity1) {
        int maxExtraSize = Staaaaaaaaaaaack.commonConfig.getMaxSize();
        if (maxExtraSize - getExtraItemCount(itemEntity) < getTotalCount(itemEntity1)
                && maxExtraSize - getExtraItemCount(itemEntity1) < getTotalCount(itemEntity)
        ) {
            return false;
        }

        ItemStack itemStack = itemEntity.getItem();
        ItemStack itemStack1 = itemEntity1.getItem();
        if (areMergableReplacementPredicate != null) {
            return areMergableReplacementPredicate.test(itemStack, itemStack1);
        }

        if (!itemStack1.getItem().equals(itemStack.getItem())) {
            return false;
        }
        if (itemStack1.hasTag() ^ itemStack.hasTag()) {
            return false;
        }
        return !itemStack1.hasTag() || Objects.equals(itemStack1.getTag(), itemStack.getTag());
    }

    public static void grow(ItemEntity entity, int count) {
        setExtraItemCount(entity, getExtraItemCount(entity) + count);
        refillItemStack(entity);
    }

    public static boolean isMergable(ItemEntity entity) {
        ItemEntityAccessor accessor = (ItemEntityAccessor) entity;
        int pickupDelay = accessor.getPickupDelay();
        int age = accessor.getAge();
        return entity.isAlive() && pickupDelay != 32767 && age != -32768 && age < 6000;
    }

    public static Optional<String> getTotalCountForDisplay(ItemEntity entity) {
        int total = getTotalCount(entity);
        boolean alwaysShowItemCount = Staaaaaaaaaaaack.clientConfig.isAlwaysShowItemCount();

        if (total >= 1_000_000_000) {
            return Optional.of(String.format("%.3fB", total/1_000_000_000f));
        }
        if (total >= 1_000_000) {
            return Optional.of(String.format("%.2fM", total/1_000_000f));
        }
        if (total >= 10_000) {
            return Optional.of(String.format("%.1fK", total/1_000f));
        }
        if (alwaysShowItemCount || total > entity.getItem().getMaxStackSize()) {
            return Optional.of(String.valueOf(total));
        }
        return Optional.empty();
    }

    public static int getTotalCount(@NotNull ItemEntity entity) {
        return entity.getItem().getCount() + getExtraItemCount(entity);
    }

    public static int getExtraItemCount(@NotNull ItemEntity entity) {
        return entity.getEntityData().get(DATA_EXTRA_ITEM_COUNT);
    }

    public static void setExtraItemCount(@NotNull ItemEntity entity, int count) {
        entity.getEntityData().set(DATA_EXTRA_ITEM_COUNT, count);
    }

    public static boolean tryRefillItemStackOnEntityRemove(@NotNull Entity entity) {
        if (!entity.getType().equals(EntityType.ITEM)) return false;

        ItemEntity itemEntity = (ItemEntity) entity;
        if (getExtraItemCount(itemEntity) <= 0) return false;

        ItemStack copied = itemEntity.getItem().copy();
        itemEntity.setItem(copied);
        copied.setCount(0);
        refillItemStack(itemEntity);
        return true;
    }

    public static void tryToMerge(@NotNull ItemEntity itemEntity, @NotNull ItemEntity itemEntity1) {
        if (Objects.equals(itemEntity.getOwner(), itemEntity1.getOwner())
                && areMergable(itemEntity, itemEntity1)
        ) {
            if (getTotalCount(itemEntity1) < getTotalCount(itemEntity)) {
                merge(itemEntity, itemEntity1);
            } else {
                merge(itemEntity1, itemEntity);
            }
        }
    }

    public static void merge(ItemEntity consumer, ItemEntity supplier) {
        ItemEntityAccessor consumerAccessor = (ItemEntityAccessor) consumer;
        ItemEntityAccessor supplierAccessor = (ItemEntityAccessor) supplier;

        consumerAccessor.setPickupDelay(Math.max(consumerAccessor.getPickupDelay(), supplierAccessor.getPickupDelay()));
        consumerAccessor.setAge(Math.min(consumerAccessor.getAge(), supplierAccessor.getAge()));

        grow(consumer, getTotalCount(supplier));

        setExtraItemCount(supplier, 0);
        supplier.setItem(ItemStack.EMPTY);
        supplier.remove();
    }

    public static boolean isBlackListItem(ItemStack itemStack) {
        if (!Staaaaaaaaaaaack.commonConfig.isEnableForUnstackableItem() && itemStack.getMaxStackSize() == 1) {
            return true;
        }

        return Staaaaaaaaaaaack.BLACK_LIST_TAG.contains(itemStack.getItem()) || Staaaaaaaaaaaack.getItemBlackList().contains(itemStack.getItem());
    }
}

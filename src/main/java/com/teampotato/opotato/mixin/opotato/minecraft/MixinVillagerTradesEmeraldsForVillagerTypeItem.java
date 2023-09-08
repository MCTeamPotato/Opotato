package com.teampotato.opotato.mixin.opotato.minecraft;

import net.minecraft.world.entity.npc.VillagerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;
import java.util.function.Consumer;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Mixin(targets = "net.minecraft.world.entity.npc.VillagerTrades$EmeraldsForVillagerTypeItem")
public abstract class MixinVillagerTradesEmeraldsForVillagerTypeItem {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", remap = false))
    private void onInit(Optional<VillagerType> instance, Consumer<VillagerType> action) {}
}

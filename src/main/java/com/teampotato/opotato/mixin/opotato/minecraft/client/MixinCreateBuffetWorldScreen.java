package com.teampotato.opotato.mixin.opotato.minecraft.client;

import net.minecraft.client.gui.screens.CreateBuffetWorldScreen;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;
import java.util.Optional;

@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "unchecked"})
@Mixin(CreateBuffetWorldScreen.class)
public abstract class MixinCreateBuffetWorldScreen {
    @Shadow private CreateBuffetWorldScreen.BiomeList list;

    @Shadow private Biome biome;

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Ljava/util/Optional;orElse(Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
    private <T> @Nullable T onInit(Optional<T> instance, T other) {
        for (CreateBuffetWorldScreen.BiomeList.Entry entry : this.list.children()) {
            if (Objects.equals(entry.biome, this.biome)) return (T) entry;
        }
        return null;
    }
}

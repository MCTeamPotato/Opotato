package com.teampotato.opotato.mixin.opotato.minecraft.client;

import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookTabButton;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Optional;

@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "unchecked"})
@Mixin(RecipeBookComponent.class)
public abstract class MixinRecipeBookComponent {
    @Shadow @Final private List<RecipeBookTabButton> tabButtons;

    @Shadow private RecipeBookTabButton selectedTab;

    @Redirect(method = "initVisuals", at = @At(value = "INVOKE", target = "Ljava/util/Optional;orElse(Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
    private <T> @Nullable T onInitVisuals(Optional<T> instance, T other) {
        for (RecipeBookTabButton tabButton : this.tabButtons) {
            if (tabButton.getCategory().equals(this.selectedTab.getCategory())) return (T) tabButton;
        }
        return null;
    }
}

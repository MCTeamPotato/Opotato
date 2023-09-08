package com.teampotato.opotato.mixin.opotato.minecraft;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.Optional;

@Mixin(RecipeManager.class)
public abstract class MixinRecipeManager {
    @Shadow protected abstract <C extends Container, T extends Recipe<C>> Map<ResourceLocation, Recipe<C>> byType(RecipeType<T> recipeType);

    @Shadow private Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipes;

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    public <C extends Container, T extends Recipe<C>> Optional<T> getRecipeFor(RecipeType<T> recipeType, C inventory, Level level) {
        for (Recipe<C> recipe : this.byType(recipeType).values()) {
            Optional<T> matchedRecipe = recipeType.tryMatch(recipe, level, inventory);
            if (matchedRecipe.isPresent()) return matchedRecipe;
        }
        return Optional.empty();
    }

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    public Optional<? extends Recipe<?>> byKey(ResourceLocation recipeId) {
        for (Map<ResourceLocation, ? extends Recipe<?>> map : this.recipes.values()) {
            Recipe<?> recipe = map.get(recipeId);
            if (recipe != null) return Optional.of(recipe);
        }
        return Optional.empty();
    }
}

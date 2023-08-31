package com.teampotato.opotato.mixin.opotato.ldlib;

import com.lowdragmc.lowdraglib.jei.JEIPlugin;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.config.*;
import mezz.jei.config.sorting.RecipeCategorySortingConfig;
import mezz.jei.gui.textures.Textures;
import mezz.jei.ingredients.IIngredientSorter;
import mezz.jei.startup.JeiStarter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = JeiStarter.class, remap = false)
public abstract class MixinJEIStarter {
    @Inject(method = "start", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lmezz/jei/Internal;setInputHandler(Lmezz/jei/input/InputHandler;)V", remap = false))
    private void onSetInputHandler(List<IModPlugin> plugins, Textures textures, IClientConfig clientConfig, IEditModeConfig editModeConfig, IIngredientFilterConfig ingredientFilterConfig, IWorldConfig worldConfig, BookmarkConfig bookmarkConfig, IModIdHelper modIdHelper, RecipeCategorySortingConfig recipeCategorySortingConfig, IIngredientSorter ingredientSorter, CallbackInfo ci) {
        JEIPlugin.setupInputHandler();
    }
}

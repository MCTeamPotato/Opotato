package com.teampotato.potatoptimize.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.teampotato.potatoptimize.PotatOptimizeCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//Just for mod commands' registry.
@Mixin(Commands.class)
public class MixinCommandManager {
    @Shadow
    @Final
    private CommandDispatcher<CommandSourceStack> dispatcher;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onRegister(Commands.CommandSelection environment, CallbackInfo callbackInfo) {
        PotatOptimizeCommand.register(dispatcher);
    }
}

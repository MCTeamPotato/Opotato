package com.teampotato.opotato.mixin.opotato.spark;

import me.lucko.spark.forge.plugin.ForgeServerSparkPlugin;
import net.minecraft.commands.CommandSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = ForgeServerSparkPlugin.class, remap = false)
public abstract class MixinForgeServerSparkPlugin {
    /**
     * @author Kasualix
     * @reason compatibility with FTB Teams
     */
    @Overwrite
    public boolean hasPermission(CommandSource sender, String permission) {
        return !(sender instanceof Player) || ((Player) sender).hasPermissions(2);
    }
}

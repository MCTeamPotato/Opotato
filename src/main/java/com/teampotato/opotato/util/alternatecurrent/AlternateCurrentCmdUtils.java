package com.teampotato.opotato.util.alternatecurrent;

import com.mojang.brigadier.Command;
import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.util.alternatecurrent.profiler.ProfilerResults;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.ITextComponent;

public class AlternateCurrentCmdUtils {

    public static int query(CommandSource source) {
        String state = Opotato.on ? "enabled" : "disabled";
        source.sendSuccess(ITextComponent.nullToEmpty(String.format("Alternate Current is currently %s", state)), false);

        return Command.SINGLE_SUCCESS;
    }

    public static int set(CommandSource source, boolean on) {
        Opotato.on = on;

        String state = Opotato.on ? "enabled" : "disabled";
        source.sendSuccess(ITextComponent.nullToEmpty(String.format("Alternate Current has been %s!", state)), true);

        return Command.SINGLE_SUCCESS;
    }

    public static int resetProfiler(CommandSource source) {
        source.sendSuccess(ITextComponent.nullToEmpty("profiler results have been cleared!"), true);

        ProfilerResults.log();
        ProfilerResults.clear();

        return Command.SINGLE_SUCCESS;
    }
}

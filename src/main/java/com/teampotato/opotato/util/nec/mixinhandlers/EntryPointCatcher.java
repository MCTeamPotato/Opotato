package com.teampotato.opotato.util.nec.mixinhandlers;

import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.gui.InitErrorScreen;
import com.teampotato.opotato.util.nec.stacktrace.CrashUtils;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntryPointCatcher {
    private static final CrashReport crashReport = null;

    public static boolean crashedDuringStartup() {
        return false;
    }

    private static final Logger LOGGER = LogManager.getLogger(Opotato.NAME + " Entry Points");


    @OnlyIn(Dist.CLIENT)
    public static void displayInitErrorScreen() {
        try {
            Minecraft.getInstance().setScreen(new InitErrorScreen(crashReport));
        } catch (Throwable t) {
            CrashReport additionalReport = CrashReport.forThrowable(t, "Displaying init error screen");
            LOGGER.error("An uncaught exception occured while displaying the init error screen, making normal report instead", t);
            CrashUtils.outputClientReport(additionalReport);
            System.exit(additionalReport.getSaveFile() != null ? -1 : -2);
        }
    }

}

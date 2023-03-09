package com.teampotato.opotato.stacktrace;

import com.ibm.icu.text.SimpleDateFormat;
import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.platform.NecPlatform;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.util.Date;


public final class CrashUtils {

    public static void outputClientReport(CrashReport report) {
        outputReport(report,true);
    }

    // We don't use the Mojang printCrashReport because it calls System.exit(), lol
    public static void outputReport(CrashReport report, boolean isClient) {
        try {
            if (report.getSaveFile() == null) {
                String reportName = "crash-";
                reportName += new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());
                reportName += isClient && Minecraft.getInstance().isSameThread() ? "-client" : "-server";
                reportName += ".txt";

                File reportsDir = new File(NecPlatform.instance().getGameDirectory().toFile(), "crash-reports");
                File reportFile = new File(reportsDir, reportName);

                report.saveToFile(reportFile);
            }
        } catch (Throwable e) {
            Opotato.LOGGER.fatal("Failed saving report", e);
        }

        Opotato.LOGGER.fatal("Minecraft ran into a problem! " + (report.getSaveFile() != null ? "Report saved to: " + report.getSaveFile() :
                "Crash report could not be saved.") + "\n" +
                report.getFriendlyReport());
    }
}

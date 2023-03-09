package com.teampotato.opotato.util.nec.mixinhandlers;

import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.config.nec.NecConfig;
import com.teampotato.opotato.gui.CrashScreen;
import com.teampotato.opotato.util.nec.stacktrace.CrashUtils;
import com.teampotato.opotato.util.nec.GlUtil;
import com.teampotato.opotato.util.nec.StateManager;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.Queue;

public class InGameCatcher {

    private static int clientCrashCount = 0;
    private static int serverCrashCount = 0;

    public static void handleClientCrash(CrashReport report) {
        clientCrashCount++;
        addInfoToCrash(report);

        resetStates();
        boolean reported = report.getException() instanceof ReportedException;
        Opotato.LOGGER.fatal(reported ? "Reported" : "Unreported" + " exception thrown!", report.getException());
        displayCrashScreen(report, clientCrashCount, true);
        // Continue game loop
        getClient().run();
    }

    private static void resetStates() {
        GlUtil.resetState();
        StateManager.resetStates();
        resetCriticalGameState();
    }

    public static void cleanupBeforeMinecraft(Queue<Runnable> renderTaskQueue) {
        if (getClient().getConnection() != null) {
            // Fix: Close the connection to avoid receiving packets from old server
            // when playing in another world (MC-128953)
            getClient().getConnection().getConnection().disconnect(new TextComponent(String.format("[%s] Client crashed", Opotato.NAME)));
        }

        getClient().clearLevel(new GenericDirtMessageScreen(new TranslatableComponent("menu.savingLevel")));

        renderTaskQueue.clear(); // Fix: method_1550(null, ...) only clears when integrated server is running
    }

    // Sometimes the game fails to reset this so we make sure it happens ourselves
    private static void resetCriticalGameState() {
        Minecraft client = getClient();
        client.player = null;
        client.level = null;
    }

    public static void handleServerCrash(CrashReport report) {
        serverCrashCount++;
        addInfoToCrash(report);
        displayCrashScreen(report, serverCrashCount, false);
    }

    private static Minecraft getClient() {
        return Minecraft.getInstance();
    }

    public static void addInfoToCrash(CrashReport report) {
        report.getSystemDetails().setDetail("Client Crashes Since Restart", () -> String.valueOf(clientCrashCount));
        report.getSystemDetails().setDetail("Integrated Server Crashes Since Restart", () -> String.valueOf(serverCrashCount));
    }

    private static void displayCrashScreen(CrashReport report, int crashCount, boolean clientCrash) {
        try {
            if (EntryPointCatcher.crashedDuringStartup()) {
                throw new IllegalStateException("Could not initialize startup crash screen");
            }
            if (crashCount > NecConfig.instance().crashLimit) {
                throw new IllegalStateException("The game has crashed an excessive amount of times");
            }

            CrashUtils.outputReport(report, clientCrash);

            // Vanilla does this when switching to main menu but not our custom crash screen
            // nor the out of memory screen (see https://bugs.mojang.com/browse/MC-128953)
            getClient().options.renderDebug = false;
            getClient().gui.getChat().clearMessages(true);

            // Display the crash screen
            getClient().setScreen(new CrashScreen(report));
        } catch (Throwable t) {
            // The crash screen has crashed. Report it normally instead.
            Opotato.LOGGER.error("An uncaught exception occured while displaying the crash screen, making normal report instead", t);
            Minecraft.crash(report);
            System.exit(report.getSaveFile() != null ? -1 : -2);
        }
    }
}


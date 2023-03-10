package com.teampotato.opotato.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.util.nec.NecLocalization;
import com.teampotato.opotato.util.nec.gui.TextWidget;
import com.teampotato.opotato.util.nec.gui.Widget;
import com.teampotato.opotato.util.nec.platform.CommonModMetadata;
import com.teampotato.opotato.util.nec.stacktrace.ModIdentifier;
import com.teampotato.opotato.util.nec.upload.CrashyUpload;
import com.teampotato.opotato.util.nec.upload.LegacyCrashLogUpload;
import net.minecraft.CrashReport;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.File;
import java.util.*;

@OnlyIn(Dist.CLIENT)
public abstract class ProblemScreen extends Screen {
    private static final Set<String> IGNORED_MODS = new HashSet<>(Arrays.asList(
            "minecraft", "fabricloader", "loadcatcher", "jumploader", "quilt_loader", "forge", "notenoughcrashes"
    ));

    private static final int GREEN = 0x00FF00;
    private static final Component uploadToCrashyText = NecLocalization.translatedText("notenoughcrashes.gui.uploadToCrashy")
            .copy().setStyle(Style.EMPTY.withColor(TextColor.fromRgb(GREEN)));
    private static final Component uploadToCrashyLoadingText = NecLocalization.translatedText("notenoughcrashes.gui.loadingCrashyUpload");

    private List<Widget> widgets = new ArrayList<>();

    protected void addWidget(Widget widget) {
        widgets.add(widget);
    }

    public abstract ProblemScreen construct(CrashReport report);

    protected final CrashReport report;
    private String uploadedCrashLink = null;
    protected int xLeft = Integer.MAX_VALUE;
    protected int xRight = Integer.MIN_VALUE;
    protected int yTop = Integer.MAX_VALUE;
    protected int yBottom = Integer.MIN_VALUE;

    protected int x;
    protected int y;


    protected ProblemScreen(CrashReport report) {
        super(new TextComponent(""));
        this.report = report;
    }


    private Component getSuspectedModsText() {
        Set<CommonModMetadata> suspectedMods = ModIdentifier.getSuspectedModsOf(report);

        // Minecraft exists and basically any stack trace, and loader exists in any launch,
        // it's better not to include them in the list of mods.
        suspectedMods.removeIf(mod -> IGNORED_MODS.contains(mod.id()));

        if (suspectedMods.isEmpty()) {
            return NecLocalization.translatedText("notenoughcrashes.crashscreen.noModsErrored");
        }

        return suspectedMods.stream()
                .sorted(Comparator.comparing(CommonModMetadata::name))
                .map(mod -> {
                    String issuesPage = mod.issuesPage();
                    MutableComponent modText = new TextComponent(mod.name());
                    if (issuesPage != null) {
                        modText.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, issuesPage)));
                    }
                    return modText;

                })
                .reduce((existing, next) -> existing.append(new TextComponent(", ")).append(next))
                .get();
    }

    private void addSuspectedModsWidget() {
        addWidget(new TextWidget(getSuspectedModsText(), TextWidget.CLICKABLE_TEXT_COLOR, font, width / 2, y + 29));
    }

    private void handleLegacyLinkClick(Button buttonWidget) {
        try {
            if (uploadedCrashLink == null) {
                uploadedCrashLink = LegacyCrashLogUpload.upload(report.getFriendlyReport());
            }
            Minecraft.getInstance().setScreen(new ConfirmLinkScreen(b -> {
                if (b) {
                    Util.getPlatform().openUri(uploadedCrashLink);
                }

                Minecraft.getInstance().setScreen(construct(report));
            }, uploadedCrashLink, true));
        } catch (Throwable e) {
            Opotato.LOGGER.error("Exception when crash menu button clicked:", e);
            buttonWidget.setMessage(NecLocalization.translatedText("notenoughcrashes.gui.failed"));
            buttonWidget.active = false;
        }
    }


    private String crashyLink = null;

    private void handleCrashyUploadClick(Button buttonWidget) {
        try {
            if (crashyLink == null) {
                buttonWidget.active = false;
                buttonWidget.setMessage(uploadToCrashyLoadingText);
                CrashyUpload.uploadToCrashy(report.getFriendlyReport()).thenAccept(link -> {
                    crashyLink = link;
                    buttonWidget.active = true;
                    buttonWidget.setMessage(uploadToCrashyText);
                    Util.getPlatform().openUri(crashyLink);
                });
            } else {
                Util.getPlatform().openUri(crashyLink);
            }
        } catch (Throwable e) {
            Opotato.LOGGER.error("Exception uploading to crashy", e);
            buttonWidget.setMessage(NecLocalization.translatedText("notenoughcrashes.gui.failed"));
            buttonWidget.active = false;
        }
    }

    @Override
    public void init() {
        widgets = new ArrayList<>();

        addButton(
                new Button(width / 2 - 155 + 160, height / 4 + 132 + 12, 150, 20,
                        NecLocalization.translatedText("notenoughcrashes.gui.getLink"),
                        this::handleLegacyLinkClick)
        );

        Button crashyButton = new Button(
                width / 2 - 155 + 160, height / 4 + 108 + 12, 150, 20, uploadToCrashyText, this::handleCrashyUploadClick
        );
        addButton(crashyButton);


        x = width / 2 - 155;
        y = height / 4;
        addSuspectedModsWidget();
    }

    @Override
    public boolean mouseClicked(double x, double y, int int_1) {
        for (Widget widget : widgets) widget.onClick(x, y);
        if (x >= xLeft && x <= xRight && y >= yTop && y <= yBottom) {
            File file = report.getSaveFile();
            if (file != null) {
                Util.getPlatform().openUri(file.toURI());
            }
        }
        return super.mouseClicked(x, y, int_1);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }


    protected void drawFileNameString(PoseStack matrixStack, int y) {
        String fileNameString = report.getSaveFile() != null ? "\u00A7n" + report.getSaveFile().getName()
                : NecLocalization.localize("notenoughcrashes.crashscreen.reportSaveFailed");
        int stLen = font.width(fileNameString);
        xLeft = width / 2 - stLen / 2;
        xRight = width / 2 + stLen / 2;
        drawString(matrixStack, font, fileNameString, xLeft, y += 11, 0x00FF00);
        yTop = y;
        yBottom = y + 10;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float delta) {
        for (Widget widget : widgets) widget.draw(matrixStack);
        super.render(matrixStack, mouseX, mouseY, delta);
    }

}

package com.teampotato.opotato.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teampotato.opotato.config.nec.NecConfig;
import com.teampotato.opotato.util.nec.NecLocalization;
import net.minecraft.CrashReport;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InitErrorScreen extends ProblemScreen {

    @Override
    public ProblemScreen construct(CrashReport report) {
        return new InitErrorScreen(report);
    }

    public InitErrorScreen(CrashReport report) {
        super(report);
    }

    private static final int TEXT_COLOR = 0xD0D0D0;

    @Override
    public void init() {
        super.init();

        Button exitButton = new Button(width / 2 - 155, height / 4 + 120 + 12, 150, 20, new TranslatableComponent("menu.quit"),
                button -> {
                    // Prevent the game from freaking out when we try to close it
                    NecConfig.instance().forceCrashScreen = false;
                    System.exit(-1);
                });

        addButton(exitButton);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int i, float f) {
        renderBackground(matrixStack);
        drawCenteredString(matrixStack, font, NecLocalization.translatedText("notenoughcrashes.initerrorscreen.title"), width / 2, height / 4 - 40, 0xFFFFFF);

        drawString(matrixStack, font, NecLocalization.localize("notenoughcrashes.initerrorscreen.summary"), x, y, TEXT_COLOR);
        drawString(matrixStack, font, NecLocalization.localize("notenoughcrashes.crashscreen.paragraph1.line1"), x, y + 18, TEXT_COLOR);


        drawString(matrixStack, font, NecLocalization.localize("notenoughcrashes.crashscreen.paragraph2.line1"), x, y + 40, TEXT_COLOR);
        drawString(matrixStack, font, NecLocalization.localize("notenoughcrashes.crashscreen.paragraph2.line2"), x, y + 49, TEXT_COLOR);

        drawFileNameString(matrixStack, y + 49);

        drawString(matrixStack, font, NecLocalization.localize("notenoughcrashes.initerrorscreen.paragraph3.line1"), x, y + 72, TEXT_COLOR);
        drawString(matrixStack, font, NecLocalization.localize("notenoughcrashes.initerrorscreen.paragraph3.line2"), x, y + 81, TEXT_COLOR);
        drawString(matrixStack, font, NecLocalization.localize("notenoughcrashes.initerrorscreen.paragraph3.line3"), x, y + 90, TEXT_COLOR);
        drawString(matrixStack, font, NecLocalization.localize("notenoughcrashes.initerrorscreen.paragraph3.line4"), x, y + 99, TEXT_COLOR);

        super.render(matrixStack, mouseX, i, f);
    }

}

package com.teampotato.opotato.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teampotato.opotato.config.nec.NecConfig;
import com.teampotato.opotato.util.nec.NecLocalization;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CrashScreen extends ProblemScreen {

    @Override
    public ProblemScreen construct(CrashReport report) {
        return new CrashScreen(report);
    }

    public CrashScreen(CrashReport report) {
        super(report);
    }

    @Override
    public void init() {
        super.init();
        Button mainMenuButton = new Button(width / 2 - 155, height / 4 + 120 + 12, 150, 20, new TranslatableComponent("gui.toTitle"),
                button -> Minecraft.getInstance().setScreen(new TitleScreen()));

        if (NecConfig.instance().disableReturnToMainMenu) {
            mainMenuButton.active = false;
            mainMenuButton.setMessage(NecLocalization.translatedText("notenoughcrashes.gui.disabledByConfig"));
        }

        addButton(mainMenuButton);
    }


    @Override
    public void render(PoseStack matrixStack, int mouseX, int i, float f) {
        renderBackground(matrixStack);
        drawCenteredString(matrixStack, font, NecLocalization.translatedText("notenoughcrashes.crashscreen.title"), width / 2, height / 4 - 40, 0xFFFFFF);

        int textColor = 0xD0D0D0;
        int x = width / 2 - 155;
        int y = height / 4;

        drawString(matrixStack, font, NecLocalization.localize("notenoughcrashes.crashscreen.summary"), x, y, textColor);
        drawString(matrixStack, font, NecLocalization.localize("notenoughcrashes.crashscreen.paragraph1.line1"), x, y += 18, textColor);

        y += 11;

        drawString(matrixStack, font, NecLocalization.localize("notenoughcrashes.crashscreen.paragraph2.line1"), x, y += 11, textColor);
        drawString(matrixStack, font, NecLocalization.localize("notenoughcrashes.crashscreen.paragraph2.line2"), x, y += 9, textColor);

        drawFileNameString(matrixStack, y);
        y += 11;

        drawString(matrixStack, font, NecLocalization.localize("notenoughcrashes.crashscreen.paragraph3.line1"), x, y += 12, textColor);
        drawString(matrixStack, font, NecLocalization.localize("notenoughcrashes.crashscreen.paragraph3.line2"), x, y += 9, textColor);
        drawString(matrixStack, font, NecLocalization.localize("notenoughcrashes.crashscreen.paragraph3.line3"), x, y += 9, textColor);
        drawString(matrixStack, font, NecLocalization.localize("notenoughcrashes.crashscreen.paragraph3.line4"), x, y + 9, textColor);

        super.render(matrixStack, mouseX, i, f);
    }

}

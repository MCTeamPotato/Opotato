package com.teampotato.opotato.util.nec.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class TextWidget implements Widget {
    private final Component text;
    private final String translated;

    private final int color;

    private final Font font;

    private final int x;
    private final int y;

    private final int width;

    final int startX;

    public static final int CLICKABLE_TEXT_COLOR = 0xE0E000;

    public TextWidget(Component text, int color, Font font, int x, int y) {
        this.text = text;
        this.color = color;
        this.font = font;
        this.x = x;
        this.y = y;
        translated = text.getString();
        width = Minecraft.getInstance().font.width(translated);
        startX = x - width / 2;
    }

    @Override
    public void draw(PoseStack stack) {
        GuiComponent.drawCenteredString(stack, font, Component.nullToEmpty(translated), x, y, color);
    }

    @Override
    public void onClick(double x, double y) {
        Component hoveredText =getTextAt(x, y);
        if (hoveredText == null) return;
        Screen screen = Minecraft.getInstance().screen;
        if (screen == null) return;
        screen.handleComponentClicked(hoveredText.getStyle());
    }

    private Component getTextAt(double x, double y) {
        if (isWithinBounds(x, y)) {
            int i = startX;

            Font renderer = Minecraft.getInstance().font;
            for (Component component : getTextParts(text)) {
                i += renderer.width(component.getContents());
                if (i > x) return component;
            }
        }
        return null;
    }

    private boolean isWithinBounds(double mouseX, double mouseY) {
        final int endX = x + width / 2;
        int height = 8;
        final int startY = y - height / 2;
        final int endY = y + height / 2;

        return mouseX >= startX && mouseX <= endX && mouseY <= endY && mouseY >= startY;
    }

    private List<Component> getTextParts(Component text) {
        List<Component> parts = new ArrayList<>();
        parts.add(text);
        parts.addAll(text.getSiblings());
        return parts;
    }
}

package com.teampotato.opotato.util.nec.gui;

import com.mojang.blaze3d.vertex.PoseStack;

public interface Widget {
    void draw(PoseStack stack);

    void onClick(double clickX, double clickY);
}

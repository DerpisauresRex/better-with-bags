package com.sajmonoriginal.betterwithbags.gui;

import java.util.concurrent.atomic.AtomicBoolean;

public interface ResponsiveBackpackScreen {
    AtomicBoolean MOUSE_ALREADY_TRANSFORMED = new AtomicBoolean(false);
    AtomicBoolean RENDERING_RESPONSIVE = new AtomicBoolean(false);

    boolean betterWithBags$hasResponsiveLayout();

    boolean betterWithBags$isRenderingResponsive();

    int betterWithBags$transformMouseX(int mouseX);

    int betterWithBags$transformMouseY(int mouseY);
}

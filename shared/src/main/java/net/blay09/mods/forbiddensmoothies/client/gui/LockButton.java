package net.blay09.mods.forbiddensmoothies.client.gui;

import net.blay09.mods.forbiddensmoothies.ForbiddenSmoothies;
import net.blay09.mods.forbiddensmoothies.menu.PrinterMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class LockButton extends Button {
    private static final ResourceLocation guiTexture = new ResourceLocation(ForbiddenSmoothies.MOD_ID, "textures/gui/printer.png");

    private final PrinterMenu menu;

    public LockButton(int x, int y, int width, int height, OnPress onPress, PrinterMenu menu) {
        super(x, y, width, height, Component.translatable("gui.forbiddensmoothies.lockButton"), onPress, DEFAULT_NARRATION);
        this.menu = menu;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        final var locked = menu.isLockedInputs();
        guiGraphics.blit(guiTexture, getX(), getY(), locked ? 216 : 196, isHovered ? 20 : 0, 20, 20);
    }
}

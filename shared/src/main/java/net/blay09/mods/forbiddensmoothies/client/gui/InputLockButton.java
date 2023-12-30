package net.blay09.mods.forbiddensmoothies.client.gui;

import net.blay09.mods.forbiddensmoothies.ForbiddenSmoothies;
import net.blay09.mods.forbiddensmoothies.menu.InputLockableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class InputLockButton extends Button {
    private static final ResourceLocation guiTexture = new ResourceLocation(ForbiddenSmoothies.MOD_ID, "textures/gui/printer.png");

    private final InputLockableMenu menu;

    public InputLockButton(int x, int y, int width, int height, OnPress onPress, InputLockableMenu menu) {
        super(x, y, width, height, Component.translatable("gui.forbiddensmoothies.lockButton"), onPress, DEFAULT_NARRATION);
        this.menu = menu;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        final var locked = menu.isLockedInputs();
        guiGraphics.blit(guiTexture, getX(), getY(), locked ? 216 : 196, isHovered ? 20 : 0, 20, 20);

        if(isHovered) {
            setTooltip(createTooltip());
        }
    }

    private Tooltip createTooltip() {
        final var component = menu.isLockedInputs() ? Component.translatable("tooltip.forbiddensmoothies.unlockInputs") : Component.translatable("tooltip.forbiddensmoothies.lockInputs");
        return Tooltip.create(component);
    }

    @Override
    protected ClientTooltipPositioner createTooltipPositioner() {
        return DefaultTooltipPositioner.INSTANCE;
    }
}

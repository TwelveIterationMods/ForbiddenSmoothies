package net.blay09.mods.forbiddensmoothies.client.gui;

import net.blay09.mods.forbiddensmoothies.ForbiddenSmoothies;
import net.blay09.mods.forbiddensmoothies.menu.PrinterMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class PowerMeterWidget extends AbstractWidget {
    private static final ResourceLocation guiTexture = new ResourceLocation(ForbiddenSmoothies.MOD_ID, "textures/gui/printer.png");
    private final PrinterMenu menu;

    public PowerMeterWidget(int x, int y, int width, int height, PrinterMenu menu) {
        super(x, y, width, height, Component.empty());
        this.menu = menu;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        var energyLost = 1f - menu.getEnergyPercentage();
        guiGraphics.blit(guiTexture, getX(), getY() + (int) (height * energyLost), 236, (int) (height * energyLost), width, (int) (height - height * energyLost));

        if (isHovered) {
            setTooltip(createTooltip());
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    private Tooltip createTooltip() {
        final var energyStored = Component.translatable("tooltip.forbiddensmoothies.energyStored", menu.getEnergyStored());
        final var energyConsumed = Component.translatable("tooltip.forbiddensmoothies.energyConsumed", menu.getEnergyPerTick());
        return Tooltip.create(energyStored.append("\n").append(energyConsumed));
    }

    @Override
    protected ClientTooltipPositioner createTooltipPositioner() {
        return DefaultTooltipPositioner.INSTANCE;
    }
}

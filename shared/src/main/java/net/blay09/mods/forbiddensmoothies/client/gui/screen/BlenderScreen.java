package net.blay09.mods.forbiddensmoothies.client.gui.screen;

import net.blay09.mods.forbiddensmoothies.ForbiddenSmoothies;
import net.blay09.mods.forbiddensmoothies.menu.BlenderMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BlenderScreen extends AbstractContainerScreen<BlenderMenu> {

    private static final ResourceLocation guiTexture = new ResourceLocation(ForbiddenSmoothies.MOD_ID, "textures/gui/blender.png");

    public BlenderScreen(BlenderMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageHeight = 222;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.setColor(1f, 1f, 1f, 1f);
        guiGraphics.blit(guiTexture, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

}

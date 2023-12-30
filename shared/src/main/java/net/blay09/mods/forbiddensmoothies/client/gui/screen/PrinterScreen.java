package net.blay09.mods.forbiddensmoothies.client.gui.screen;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.forbiddensmoothies.ForbiddenSmoothies;
import net.blay09.mods.forbiddensmoothies.client.gui.LockButton;
import net.blay09.mods.forbiddensmoothies.client.gui.PowerMeterWidget;
import net.blay09.mods.forbiddensmoothies.menu.PrinterMenu;
import net.blay09.mods.forbiddensmoothies.network.SetInputLockMessage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PrinterScreen extends AbstractContainerScreen<PrinterMenu> {

    private static final ResourceLocation guiTexture = new ResourceLocation(ForbiddenSmoothies.MOD_ID, "textures/gui/printer.png");

    public PrinterScreen(PrinterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageHeight = 222;
        inventoryLabelY = imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();

        final var lockButton = new LockButton(leftPos + 7, topPos + 17, 20, 20, button -> {
            Balm.getNetworking().sendToServer(new SetInputLockMessage(!menu.isLockedInputs()));
            menu.setLockedInputs(!menu.isLockedInputs());
        }, menu);
        addRenderableWidget(lockButton);

        addRenderableOnly(new PowerMeterWidget(leftPos + 152, topPos + 9, 16, 70, menu));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.setColor(1f, 1f, 1f, 1f);
        guiGraphics.blit(guiTexture, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        var progress = this.menu.getProgress();
        guiGraphics.blit(guiTexture, leftPos + 74, topPos + 56, 176, 60, 28, (int) (28 * progress));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        if(menu.isLockedInputs()) {
            final var pose = guiGraphics.pose();
            pose.pushPose();
            pose.translate(0f, 0f, 300);
            for (int i = 0; i < 8; i++) {
                final var slot = menu.getSlot(i);
                final var slotStack = slot.getItem();
                if (slotStack.getCount() == 1) {
                    guiGraphics.fillGradient(leftPos + slot.x, topPos + slot.y, leftPos + slot.x + 16, topPos + slot.y + 16, 0x60FFFFFF, 0x90FFFFFF);
                }
            }
            pose.popPose();
        }
    }

}

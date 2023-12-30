package net.blay09.mods.forbiddensmoothies.menu;

import net.blay09.mods.forbiddensmoothies.block.entity.PrinterBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public class PrinterMenu extends AbstractContainerMenu {

    private final Player player;
    private final Container container;
    private final ContainerData data;

    public PrinterMenu(MenuType<PrinterMenu> menuType, int windowId, Player player) {
        this(menuType, windowId, player, new SimpleContainer(26), new SimpleContainerData(6));
    }

    public PrinterMenu(MenuType<PrinterMenu> menuType, int windowId, Player player, Container container, ContainerData containerData) {
        super(menuType, windowId);
        this.player = player;
        this.container = container;
        this.data = containerData;
        checkContainerSize(container, 26);
        checkContainerDataCount(containerData, 6);

        for (int i = 0; i < 5; i++) {
            addSlot(new Slot(container, i, 44 + i * 18, 18));
        }
        for (int i = 0; i < 3; i++) {
            addSlot(new Slot(container, 5 + i, 62 + i * 18, 36));
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new OutputSlot(container, 8 + j + i * 9, 8 + j * 18, 88 + i * 18));
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(player.getInventory(), j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(player.getInventory(), i, 8 + i * 18, 198));
        }

        addDataSlots(containerData);
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotNumber) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(slotNumber);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();
            if (slotNumber < 26) {
                if (!moveItemStackTo(slotStack, 26, 62, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (true) {
                if (!moveItemStackTo(slotStack, 0, 8, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, slotStack);
        }
        return itemStack;
    }

    public float getPrintProgress() {
        return data.get(PrinterBlockEntity.DATA_PRINTING_PROGRESS) / (float) data.get(PrinterBlockEntity.DATA_PRINTING_TOTAL_TIME);
    }

    public boolean isLockedInputs() {
        return data.get(PrinterBlockEntity.DATA_LOCKED_INPUTS) != 0;
    }

    public void setLockedInputs(boolean locked) {
        setData(PrinterBlockEntity.DATA_LOCKED_INPUTS, locked ? 1 : 0);
    }

    public int getEnergyStored() {
        return data.get(PrinterBlockEntity.DATA_ENERGY);
    }

    public int getEnergyCapacity() {
        return data.get(PrinterBlockEntity.DATA_ENERGY_TOTAL);
    }

    public float getEnergyPercentage() {
        return getEnergyStored() / (float) getEnergyCapacity();
    }

    public int getEnergyPerTick() {
        return data.get(PrinterBlockEntity.DATA_ENERGY_CONSUMPTION);
    }
}

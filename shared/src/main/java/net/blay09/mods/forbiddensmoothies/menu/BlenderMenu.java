package net.blay09.mods.forbiddensmoothies.menu;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public class BlenderMenu extends AbstractContainerMenu {

    private final Player player;
    private final Container container;
    private final ContainerData data;

    public BlenderMenu(MenuType<BlenderMenu> menuType, int windowId, Player player) {
        this(menuType, windowId, player, new SimpleContainer(3), new SimpleContainerData(2));
    }

    public BlenderMenu(MenuType<BlenderMenu> menuType, int windowId, Player player, Container container, ContainerData containerData) {
        super(menuType, windowId);
        this.player = player;
        this.container = container;
        this.data = containerData;
        checkContainerSize(container, 3);
        checkContainerDataCount(containerData, 2);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(player.getInventory(), j + i * 9 + 9, 8 + j * 18, 92 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(player.getInventory(), i, 8 + i * 18, 150));
        }

        addDataSlots(containerData);
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        // TODO Fix me or I will crash
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();
            if (slotIndex >= 56 && slotIndex < 65) {
                if (!moveItemStackTo(slotStack, 29, 56, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= 29 && slotIndex < 56) {
                if (!moveItemStackTo(slotStack, 56, 65, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemStack;
    }

}

package net.blay09.mods.forbiddensmoothies.menu;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class OutputSlot extends Slot {
    public OutputSlot(Container container, int index, int x, int y) {
        super(container, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return false;
    }
}

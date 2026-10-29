package fr.alessdevita.valtherion.client.gui.inventory;

import fr.alessdevita.valtherion.item.ItemStack;

public class InventoryCraftResult implements IInventory {
    private ItemStack stack;

    @Override
    public void markDirty() {
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex) {
        return slotIndex == 0 ? this.stack : null;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int amount) {
        if (slotIndex != 0 || this.stack == null) {
            return null;
        }
        ItemStack copy = this.stack.copy();
        this.stack = null;
        return copy;
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack stack) {
        if (slotIndex == 0) {
            this.stack = stack;
        }
    }
}

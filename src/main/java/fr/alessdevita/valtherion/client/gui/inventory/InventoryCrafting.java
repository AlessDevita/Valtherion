package fr.alessdevita.valtherion.client.gui.inventory;

import fr.alessdevita.valtherion.item.ItemStack;

public class InventoryCrafting implements IInventory {
    private final ItemStack[] stacks;

    public InventoryCrafting(Container container, int width, int height) {
        this.stacks = new ItemStack[width * height];
    }

    @Override
    public void markDirty() {
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex) {
        return slotIndex >= 0 && slotIndex < this.stacks.length ? this.stacks[slotIndex] : null;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int amount) {
        if (this.stacks[slotIndex] == null) {
            return null;
        }
        ItemStack stack = this.stacks[slotIndex];
        ItemStack copy = stack.copy();
        int removed = Math.min(amount, stack.stackSize);
        stack.stackSize -= removed;
        if (stack.stackSize <= 0) {
            this.stacks[slotIndex] = null;
        }
        return copy;
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack stack) {
        if (slotIndex >= 0 && slotIndex < this.stacks.length) {
            this.stacks[slotIndex] = stack;
        }
    }

    public ItemStack getStackInSlotOnClosing(int index) {
        return this.getStackInSlot(index);
    }
}

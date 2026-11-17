package fr.alessdevita.valtherion.client.gui.inventory;

import fr.alessdevita.valtherion.item.ItemStack;

public class TileEntityFurnace {
    private int cookTime;
    private int totalCookTime;
    private int furnaceBurnTime;
    private int currentItemBurnTime;
    private final ItemStack[] itemStacks = new ItemStack[3];

    public static boolean isItemFuel(ItemStack stack) {
        return stack != null && stack.getItem() != null;
    }

    public int getField(int id) {
        switch (id) {
            case 0: return this.cookTime;
            case 1: return this.furnaceBurnTime;
            case 2: return this.currentItemBurnTime;
            case 3: return this.totalCookTime;
            default: return 0;
        }
    }

    public void setField(int id, int value) {
        switch (id) {
            case 0: this.cookTime = value; break;
            case 1: this.furnaceBurnTime = value; break;
            case 2: this.currentItemBurnTime = value; break;
            case 3: this.totalCookTime = value; break;
        }
    }

    public ItemStack getStackInSlot(int index) {
        if (index < 0 || index >= this.itemStacks.length) {
            return null;
        }
        return this.itemStacks[index];
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index >= 0 && index < this.itemStacks.length) {
            this.itemStacks[index] = stack;
        }
    }

    public void markDirty() {
    }

    public boolean isUseableByPlayer(Object player) {
        return true;
    }

    public void openInventory(Object player) {
    }
}

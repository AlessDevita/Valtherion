package fr.alessdevita.valtherion.client.gui.inventory;

import fr.alessdevita.valtherion.item.ItemStack;

public class TileEntityBrewingStand {
    private int brewTime;
    private int totalBrewTime;
    private int fuelRemaining;
    private int totalFuelTime;
    private final ItemStack[] itemStacks = new ItemStack[4];

    public static boolean isItemFuel(ItemStack stack) {
        return stack != null && stack.getItem() != null;
    }

    public int getField(int id) {
        switch (id) {
            case 0: return this.brewTime;
            case 1: return this.fuelRemaining;
            case 2: return this.totalFuelTime;
            case 3: return this.totalBrewTime;
            default: return 0;
        }
    }

    public void setField(int id, int value) {
        switch (id) {
            case 0: this.brewTime = value; break;
            case 1: this.fuelRemaining = value; break;
            case 2: this.totalFuelTime = value; break;
            case 3: this.totalBrewTime = value; break;
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

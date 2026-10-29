package fr.alessdevita.valtherion.client.gui.inventory;

import fr.alessdevita.valtherion.item.ItemStack;

public class InventoryPlayer implements IInventory {
    public final EntityPlayer player;

    public InventoryPlayer(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex) {
        return null;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int amount) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack stack) {
    }

    public boolean isUseableByPlayer(Object player) {
        return true;
    }
}

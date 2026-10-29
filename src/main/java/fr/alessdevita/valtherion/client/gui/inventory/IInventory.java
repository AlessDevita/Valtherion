package fr.alessdevita.valtherion.client.gui.inventory;

import fr.alessdevita.valtherion.item.ItemStack;

public interface IInventory {
    void markDirty();

    ItemStack getStackInSlot(int slotIndex);

    int getInventoryStackLimit();

    ItemStack decrStackSize(int slotIndex, int amount);

    void setInventorySlotContents(int slotIndex, ItemStack stack);

    default boolean isUseableByPlayer(Object player) {
        return true;
    }

    default void openInventory(Object player) {
    }

    default void closeInventory(Object player) {
    }

    default int getField(int id) {
        return 0;
    }

    default void setField(int id, int value) {
    }
}

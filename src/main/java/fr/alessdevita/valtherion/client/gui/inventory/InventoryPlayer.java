package fr.alessdevita.valtherion.client.gui.inventory;

import fr.alessdevita.valtherion.entity.player.EntityPlayer;
import fr.alessdevita.valtherion.item.ItemStack;

public class InventoryPlayer implements IInventory {
    public final EntityPlayer player;
    private final ItemStack[] mainInventory = new ItemStack[27];
    private final ItemStack[] armorInventory = new ItemStack[4];
    private int currentSlotIndex;

    public InventoryPlayer(EntityPlayer player) {
        this.player = player;
        this.currentSlotIndex = 0;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= 36) {
            return null;
        }
        if (slotIndex < 27) {
            return this.mainInventory[slotIndex];
        }
        return this.armorInventory[slotIndex - 27];
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int amount) {
        ItemStack stack = this.getStackInSlot(slotIndex);
        if (stack == null) {
            return null;
        }

        ItemStack copy;
        if (amount >= stack.getCount()) {
            copy = stack;
            this.setInventorySlotContents(slotIndex, null);
        } else {
            copy = stack.copy();
            copy.setCount(amount);
            stack.setCount(stack.getCount() - amount);
        }
        return copy;
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack stack) {
        if (slotIndex < 0 || slotIndex >= 36) {
            return;
        }
        if (slotIndex < 27) {
            this.mainInventory[slotIndex] = stack;
        } else {
            this.armorInventory[slotIndex - 27] = stack;
        }
    }

    /**
     * Retourne l'item actuellement sélectionné dans la barre d'expérience.
     */
    public ItemStack getCurrentItem() {
        return this.getStackInSlot(this.currentSlotIndex + 36);
    }

    /**
     * Change le slot sélectionné dans la barre d'expérience.
     */
    public void setCurrentItemSlot(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < 9) {
            this.currentSlotIndex = slotIndex;
        }
    }

    public boolean isUseableByPlayer(Object player) {
        return true;
    }
}

package fr.alessdevita.valtherion.client.gui.inventory;

import fr.alessdevita.valtherion.item.ItemStack;

/**
 * Container du distributeur : grille 3x3 + inventaire joueur.
 * Referencé par GuiDispenser.
 * TODO: TileEntityDispenser doit exister dans fr.alessdevita.valtherion.tileentity.
 */
public class ContainerDispenser extends Container
{
    private IInventory tileDispenser;

    public ContainerDispenser(InventoryPlayer playerInventory, IInventory dispenserInventory)
    {
        this.tileDispenser = dispenserInventory;

        for (int y = 0; y < 3; ++y)
        {
            for (int x = 0; x < 3; ++x)
            {
                this.addSlotToContainer(new Slot(dispenserInventory, x + y * 3, 62 + x * 18, 17 + y * 18));
            }
        }

        this.addPlayerSlots(8, 84);
    }

    @Override
    public boolean canInteractWith(Object player) {
        return this.tileDispenser.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(Object player, int index)
    {
        ItemStack itemstack = null;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 9) {
                if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 9, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }
}
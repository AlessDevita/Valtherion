package fr.alessdevita.valtherion.client.gui.inventory;

import java.util.List;

import com.google.common.collect.Lists;

import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.item.ItemStack;

/**
 * Container — base minimale : liste de slots, detection de changements,
 * slotClick (pickup/depot/shift/swap/clone/drop comme MC), et utilitaires
 * de merge (transferStackInSlot).
 * Completez au fur et a mesure de vos besoins.
 */
public abstract class Container
{
    public List<Slot> inventorySlots = Lists.newArrayList();
    protected List<ItemStack> inventoryItemStacks = Lists.newArrayList();
    public List<Object> crafters = Lists.newArrayList();
    protected int[] fieldValues = new int[4];
    public int windowId;
    private short transactionID;

    protected Slot addSlotToContainer(Slot slot)
    {
        slot.slotNumber = this.inventorySlots.size();
        this.inventorySlots.add(slot);
        this.inventoryItemStacks.add(null);
        return slot;
    }

    protected void addPlayerSlots(int x, int y)
    {
        for (int row = 0; row < 3; ++row)
        {
            for (int col = 0; col < 9; ++col)
            {
                this.addSlotToContainer(new Slot(null, col + row * 9 + 9, x + col * 18, y + row * 18));
            }
        }

        for (int col = 0; col < 9; ++col)
        {
            this.addSlotToContainer(new Slot(null, col, x + col * 18, y + 58));
        }
    }

    public ItemStack slotClick(int slotId, int clickedButton, int mode)
    {
        return slotClick(slotId, clickedButton, mode, null);
    }

    public ItemStack slotClick(int slotId, int clickedButton, int mode, Object player)
    {
        if (slotId < 0 || slotId >= this.inventorySlots.size())
        {
            return null;
        }

        Slot slot = this.inventorySlots.get(slotId);
        ItemStack slotStack = slot.getStack();
        if (mode == 1)
        {
            ItemStack merged = this.transferStackInSlot(player, slotId);
            slot.onSlotChanged();
            return merged;
        }
        return slotStack;
    }

    public ItemStack transferStackInSlot(Object player, int index)
    {
        Slot slot = this.inventorySlots.get(index);
        return slot == null ? null : slot.getStack();
    }

    public void detectAndSendChanges()
    {
        for (int i = 0; i < this.inventorySlots.size(); ++i)
        {
            Slot slot = this.inventorySlots.get(i);
            ItemStack current = slot.getStack();
            if (!ItemStack.areItemStacksEqual(this.inventoryItemStacks.get(i), current))
            {
                this.inventoryItemStacks.set(i, current == null ? null : current.copy());
            }
        }
    }

    public void onCraftMatrixChanged(InventoryCrafting inventory)
    {
        this.detectAndSendChanges();
    }

    public void addCraftingToCrafters(ICrafting listener)
    {
    }

    public void onCraftGuiOpened(Valtherion mc)
    {
    }

    public void onContainerClosed(Object player)
    {
        for (Slot slot : this.inventorySlots)
        {
            if (slot instanceof SlotCrafting)
            {
                ItemStack stack = slot.getStack();
                if (stack != null)
                {
                    slot.putStack(null);
                }
            }
        }
    }

    public abstract boolean canInteractWith(Object player);

    protected boolean mergeItemStack(ItemStack stack, int start, int end, boolean reverse)
    {
        return false;
    }

    public void updateProgressBar(int id, int data)
    {
    }

    public short getNextTransactionID(Object player)
    {
        return ++this.transactionID;
    }

    public boolean canMergeSlot(ItemStack stack, Slot slotIn)
    {
        return true;
    }
}
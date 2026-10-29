package fr.alessdevita.valtherion.client.gui.inventory;

import fr.alessdevita.valtherion.item.ItemStack;

public class ContainerPlayer extends Container {
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);
    public IInventory craftResult = new InventoryCraftResult();
    public InventoryPlayer invPlayer;

    public ContainerPlayer(InventoryPlayer playerInv)
    {
        this.invPlayer = playerInv;
        this.craftResult = new InventoryCraftResult();
        this.addSlotToContainer(new SlotCrafting(this, this.craftMatrix, this.craftResult, 0, 144, 36));

        for (int y = 0; y < 2; ++y)
        {
            for (int x = 0; x < 2; ++x)
            {
                this.addSlotToContainer(new Slot(this.craftMatrix, x + y * 2, 88 + x * 18, 26 + y * 18));
            }
        }

        for (int k = 0; k < 4; ++k)
        {
            final int armorSlot = k;
            this.addSlotToContainer(new Slot(playerInv, 36 + (3 - k), 8, 8 + k * 18)
            {
                public int getSlotStackLimit()
                {
                    return 1;
                }

                public boolean isItemValid(ItemStack stack)
                {
                    return stack != null && stack.getItem() != null
                            && stack.getItem().isValidArmor(stack, armorSlot, invPlayer.player);
                }
            });
        }

        this.addPlayerSlots(8, 84);
    }

    @Override
    public void onCraftMatrixChanged(InventoryCrafting inventory)
    {
        this.craftResult.setInventorySlotContents(0, null);
    }

    @Override
    public void onContainerClosed(Object player)
    {
        super.onContainerClosed(player);
        for (int i = 0; i < 4; ++i)
        {
            ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);
            if (itemstack != null)
            {
                ((EntityPlayer) player).dropPlayerItemWithRandomChoice(itemstack, false);
            }
        }
    }

    @Override
    public boolean canInteractWith(Object player)
    {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(Object player, int index)
    {
        return null;
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn)
    {
        return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
    }
}

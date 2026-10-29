package fr.alessdevita.valtherion.client.gui.inventory;

import fr.alessdevita.valtherion.item.ItemStack;
import fr.alessdevita.valtherion.item.crafting.FurnaceRecipes;

/**
 * Container du four : slot entree, slot combustible, slot sortie + inventaire joueur.
 * GuiFurnace appelle getCookProgress() (0..24) et getBurnTimeRemaining() (0..13).
 * TODO: TileEntityFurnace doit exister dans fr.alessdevita.valtherion.tileentity.
 */
public class ContainerFurnace extends Container
{
    private IInventory tileFurnace;

    public ContainerFurnace(InventoryPlayer playerInventory, IInventory furnaceInventory)
    {
        this.tileFurnace = furnaceInventory;
        this.addSlotToContainer(new Slot(furnaceInventory, 0, 56, 17));          // entree
        this.addSlotToContainer(new SlotFurnaceFuel(furnaceInventory, 1, 56, 53)); // combustible
        this.addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, furnaceInventory, 2, 116, 35)); // sortie

        for (int y = 0; y < 3; ++y)
        {
            for (int x = 0; x < 9; ++x)
            {
                this.addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for (int x = 0; x < 9; ++x)
        {
            this.addSlotToContainer(new Slot(playerInventory, x, 8 + x * 18, 142));
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting listener)
    {
        super.addCraftingToCrafters(listener);
        // TODO: envoyer les champs de progression (field 0 = cookTime, 1 = burnerTime, 2 = currentItemBurnTime, 3 = totalCookTime)
        listener.sendProgressBarUpdate(this, 0, this.tileFurnace.getField(0));
        listener.sendProgressBarUpdate(this, 1, this.tileFurnace.getField(1));
        listener.sendProgressBarUpdate(this, 2, this.tileFurnace.getField(2));
        listener.sendProgressBarUpdate(this, 3, this.tileFurnace.getField(3));
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);

            for (int j = 0; j < 4; ++j)
            {
                int k = this.tileFurnace.getField(j);

                if (this.fieldValues[j] != k)
                {
                    icrafting.sendProgressBarUpdate(this, j, k);
                }
            }
        }

        for (int j = 0; j < 4; ++j)
        {
            this.fieldValues[j] = this.tileFurnace.getField(j);
        }
    }

    @Override
    public boolean canInteractWith(Object player) {
        return this.tileFurnace.isUseableByPlayer(player);
    }

    @Override
    public void updateProgressBar(int id, int data)
    {
        this.tileFurnace.setField(id, data);
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

            if (index == 2)
            {
                if (!this.mergeItemStack(itemstack1, 3, 39, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index != 1 && index != 0)
            {
                if (FurnaceRecipes.instance().getSmeltingResult(itemstack1) != null)
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (SlotFurnaceFuel.isItemFuel(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                    {
                        return null;
                    }
                }
                else if (index >= 3 && index < 30)
                {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false))
                    {
                        return null;
                    }
                }
                else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 39, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack) null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }

    public int getCookProgress()
    {
        return this.getCookProgress(24);
    }

    public int getBurnTimeRemaining()
    {
        return this.getBurnTimeRemaining(13);
    }

    /** Progression de cuisson pour la fleche (0..24). Requis par GuiFurnace. */
    @SideOnly(Side.CLIENT)
    public int getCookProgress(int pixels)
    {
        int cookTime = this.tileFurnace.getField(2);
        int totalCookTime = this.tileFurnace.getField(3);
        return totalCookTime != 0 && cookTime != 0 ? cookTime * pixels / totalCookTime : 0;
    }

    /** Flamme du four (0..12). Requis par GuiFurnace. */
    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemaining(int pixels)
    {
        int currentBurnTime = this.tileFurnace.getField(1);
        int totalBurnTime = this.tileFurnace.getField(0);
        return totalBurnTime == 0 ? 0 : currentBurnTime * pixels / totalBurnTime;
    }

    /** Slot combustible. */
    public static class SlotFurnaceFuel extends Slot
    {
        public SlotFurnaceFuel(IInventory inventoryIn, int index, int x, int y)
        {
            super(inventoryIn, index, x, y);
        }

        public boolean isItemValid(ItemStack stack)
        {
            return isItemFuel(stack); // TODO: TileEntityFurnace.getItemBurnTime
        }

        public static boolean isItemFuel(ItemStack stack)
        {
            // TODO: deleguer a la classe four de Valtherion
            return TileEntityFurnace.isItemFuel(stack);
        }
    }

    /** Slot de sortie : seul le retrait est autorise, declenche l'achievement/statistique de cuisson. */
    public static class SlotFurnaceOutput extends Slot
    {
        private EntityPlayer player;
        private int removeCount;

        public SlotFurnaceOutput(EntityPlayer player, IInventory inventoryIn, int index, int x, int y)
        {
            super(inventoryIn, index, x, y);
            this.player = player;
        }

        public boolean isItemValid(ItemStack stack)
        {
            return false;
        }

        public ItemStack decrStackSize(int amount)
        {
            if (this.getHasStack())
            {
                this.removeCount += Math.min(amount, this.getStack().stackSize);
                return super.decrStackSize(amount);
            }

            return null;
        }

        public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
        {
            this.onCrafting(stack);
            super.onPickupFromSlot(playerIn, stack);
        }

        protected void onCrafting(ItemStack stack, int amount)
        {
            this.removeCount += amount;
            this.onCrafting(stack);
        }

        protected void onCrafting(ItemStack stack)
        {
            stack.onCrafting(this.player, this.removeCount);
            // TODO: statistiques / achievements de cuisson de Valtherion
            this.removeCount = 0;
        }
    }
}
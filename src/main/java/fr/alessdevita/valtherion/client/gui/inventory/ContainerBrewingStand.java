package fr.alessdevita.valtherion.client.gui.inventory;

import fr.alessdevita.valtherion.item.ItemPotion;
import fr.alessdevita.valtherion.item.ItemStack;
import fr.alessdevita.valtherion.item.potion.PotionHelper;

/**
 * Container de l'alambic : 1 slot ingredient + 3 fioles + inventaire joueur.
 * GuiBrewingStand appelle getBrewProgress() (0..29) et getFuelRemaining() (0..20).
 * TODO: TileEntityBrewingStand et InventoryBrewingStand doivent exister dans fr.alessdevita.valtherion.tileentity.
 */
public class ContainerBrewingStand extends Container
{
    private IInventory tileBrewingStand;
    /** Slot de combustible (blaze powder). */
    private final BrewingStandFuelSlot slotFuel;
    private final BrewingStandIngredientSlot slotIngredient;
    /** Derniere valeur du champ de brewTime pour l'update progressive. */
    private int brewTime;

    public ContainerBrewingStand(InventoryPlayer playerInventory, IInventory brewingStandInventory)
    {
        this.tileBrewingStand = brewingStandInventory;
        this.addSlotToContainer(this.slotFuel = new ContainerBrewingStand.BrewingStandFuelSlot(brewingStandInventory, 4, 44, 65));
        this.addSlotToContainer(this.slotIngredient = new ContainerBrewingStand.BrewingStandIngredientSlot(brewingStandInventory, 3, 79, 41));

        for (int k = 0; k < 3; ++k)
        {
            this.addSlotToContainer(new ContainerBrewingStand.BrewingStandPotionSlot(
                    playerInventory.player, brewingStandInventory, k, 58 + k * 18, 44));
        }

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
        listener.sendProgressBarUpdate(this, 0, this.tileBrewingStand.getField(0));
        listener.sendProgressBarUpdate(this, 1, this.tileBrewingStand.getField(1));
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);

            if (this.brewTime != this.tileBrewingStand.getField(0))
            {
                icrafting.sendProgressBarUpdate(this, 0, this.tileBrewingStand.getField(0));
            }
        }

        this.brewTime = this.tileBrewingStand.getField(0);
    }

    @Override
    public boolean canInteractWith(Object player) {
        return this.tileBrewingStand.isUseableByPlayer(player);
    }

    @Override
    public void updateProgressBar(int id, int data)
    {
        this.tileBrewingStand.setField(id, data);
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

            if ((index < 0 || index > 4) && index >= 5 && index < 32)
            {
                if (!this.mergeItemStack(itemstack1, 5, 32, false))
                {
                    return null;
                }
            }
            else if (index >= 32 && index < 41)
            {
                if (!this.mergeItemStack(itemstack1, 5, 32, false))
                {
                    return null;
                }
            }
            else if (index >= 5 && index < 32 && PotionHelper.isIngredient(itemstack1))
            {
                if (!this.mergeItemStack(itemstack1, 3, 4, false))
                {
                    return null;
                }
            }
            else if (index >= 5 && index < 32 && TileEntityBrewingStand.isItemFuel(itemstack1))
            {
                if (!this.mergeItemStack(itemstack1, 4, 5, false))
                {
                    return null;
                }
            }
            else if (index >= 5 && index < 32 && BrewingStandPotionSlot.canHoldPotion(itemstack1))
            {
                if (!this.mergeItemStack(itemstack1, 0, 3, false))
                {
                    return null;
                }
            }
            else if (index == 4)
            {
                if (!this.mergeItemStack(itemstack1, 5, 41, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index == 3)
            {
                if (!this.mergeItemStack(itemstack1, 5, 41, false))
                {
                    return null;
                }
            }
            else if (index >= 0 && index <= 2)
            {
                if (!this.mergeItemStack(itemstack1, 5, 41, false))
                {
                    return null;
                }
            }
            else
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

    public int getBrewProgress()
    {
        return this.getBrewProgress(29);
    }

    public int getFuelRemaining()
    {
        return this.getFuelRemaining(20);
    }

    @SideOnly(Side.CLIENT)
    public int getBrewProgress(int pixels)
    {
        int j = this.tileBrewingStand.getField(0);
        int k = 400;
        return j != 0 && k != 0 ? j * pixels / k : 0;
    }

    @SideOnly(Side.CLIENT)
    public int getFuelRemaining(int pixels)
    {
        int i = this.tileBrewingStand.getField(1);
        int j = 20;
        return i != 0 ? i * pixels / j : 0;
    }

    /** Slot de fiole : ne peut etre modifie par le joueur, declenche la stat/achievement de potion. */
    class BrewingStandPotionSlot extends Slot
    {
        private EntityPlayer player;

        public BrewingStandPotionSlot(EntityPlayer playerIn, IInventory inventoryIn, int index, int x, int y)
        {
            super(inventoryIn, index, x, y);
            this.player = playerIn;
        }

        public boolean isItemValid(ItemStack stack)
        {
            return canHoldPotion(stack);
        }

        public int getSlotStackLimit()
        {
            return 1;
        }

        public ItemStack onTake(Object player, ItemStack stack)
        {
            return stack;
        }

        public static boolean canHoldPotion(ItemStack stack)
        {
            return stack != null && (stack.getItem() != null && (stack.getItem().getClass().getSimpleName().contains("Potion") || stack.getItem() == Items.glass_bottle));
        }

        public void onSlotChanged()
        {
            super.onSlotChanged();
            ContainerBrewingStand.this.tileBrewingStand.markDirty();
        }
    }

    /** Slot d'ingredient. */
    class BrewingStandIngredientSlot extends Slot
    {
        public BrewingStandIngredientSlot(IInventory inventoryIn, int index, int x, int y)
        {
            super(inventoryIn, index, x, y);
        }

        public boolean isItemValid(ItemStack stack)
        {
            return PotionHelper.isIngredient(stack);
        }

        public void onSlotChanged()
        {
            super.onSlotChanged();
            ContainerBrewingStand.this.tileBrewingStand.markDirty();
        }
    }

    /** Slot de base (combustible, ex: poudre de blaze). */
    class BrewingStandFuelSlot extends Slot
    {
        public BrewingStandFuelSlot(IInventory inventoryIn, int index, int x, int y)
        {
            super(inventoryIn, index, x, y);
        }

        public boolean isItemValid(ItemStack stack)
        {
            return TileEntityBrewingStand.isItemFuel(stack);
        }

        public void onSlotChanged()
        {
            super.onSlotChanged();
            ContainerBrewingStand.this.tileBrewingStand.markDirty();
        }
    }
}
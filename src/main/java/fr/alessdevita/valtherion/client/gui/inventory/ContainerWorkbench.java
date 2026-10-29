package fr.alessdevita.valtherion.client.gui.inventory;

import fr.alessdevita.valtherion.item.ItemStack;

/**
 * Container de la table de craft : grille 3x3 + slot de resultat + inventaire joueur.
 * Referencé par GuiCrafting.
 */
public class ContainerWorkbench extends Container
{
    /** Grille de craft 3x3. */
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    /** Slot de resultat. */
    public IInventory craftResult = new InventoryCraftResult();
    /** Le monde (pour la recette + verification de distance). */
    private World worldObj;
    /** Position du bloc table de craft. */
    private BlockPos pos;
    private EntityPlayer player;

    public ContainerWorkbench(InventoryPlayer playerInv, World worldIn, BlockPos posIn)
    {
        this.worldObj = worldIn;
        this.pos = posIn;
        this.player = playerInv.player;
        this.addSlotToContainer(new SlotCrafting(this, this.craftMatrix, this.craftResult, 0, 124, 35));

        for (int y = 0; y < 3; ++y)
        {
            for (int x = 0; x < 3; ++x)
            {
                this.addSlotToContainer(new Slot(this.craftMatrix, x + y * 3, 30 + x * 18, 17 + y * 18));
            }
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

        if (!this.worldObj.isRemote)
        {
            for (int i = 0; i < 9; ++i)
            {
                ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);

                if (itemstack != null)
                {
                    ((EntityPlayer) player).dropPlayerItemWithRandomChoice(itemstack, false);
                }
            }
        }
    }

    @Override
    public boolean canInteractWith(Object player)
    {
        return this.worldObj.getBlockState(this.pos).getBlock() != Blocks.crafting_table
                ? false
                : ((EntityPlayer) player).getDistanceSq((double) this.pos.getX() + 0.5D,
                (double) this.pos.getY() + 0.5D,
                (double) this.pos.getZ() + 0.5D) <= 64.0D;
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
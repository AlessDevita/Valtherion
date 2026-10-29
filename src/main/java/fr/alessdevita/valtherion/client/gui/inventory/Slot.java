package fr.alessdevita.valtherion.client.gui.inventory;

import fr.alessdevita.valtherion.item.ItemStack;

/* ------------------------------------------------------------------ */
/* Slot — un emplacement d'inventaire affiche dans la GUI.             */
/* ------------------------------------------------------------------ */

public class Slot
{
    /** Inventaire logique contenant ce slot. */
    public final IInventory inventory;
    /** Index dans l'inventaire logique. */
    public final int slotIndex;
    /** Position d'affichage dans la GUI (pixels, relatifs a guiLeft/guiTop). */
    public int xDisplayPosition;
    public int yDisplayPosition;
    /** Numero du slot dans le Container (assigne par addSlotToContainer). */
    public int slotNumber;

    public Slot(IInventory inventory, int index, int x, int y)
    {
        this.inventory = inventory;
        this.slotIndex = index;
        this.xDisplayPosition = x;
        this.yDisplayPosition = y;
    }

    public void onSlotChanged()
    {
        this.inventory.markDirty();
    }

    /** Stack contenue (peut etre null). */
    public ItemStack getStack()
    {
        return this.inventory.getStackInSlot(this.slotIndex);
    }

    /** Verifie si le slot peut contenir cette stack. */
    public boolean isItemValid(ItemStack stack)
    {
        return true;
    }

    public boolean getHasStack()
    {
        return this.getStack() != null;
    }

    /** Nombre max empilable dans ce slot (delegue a l'inventaire/item). */
    public int getSlotStackLimit(ItemStack stack)
    {
        return this.inventory.getInventoryStackLimit();
    }

    /** Preleve la stack (toute ou partiellement). */
    public ItemStack decrStackSize(int amount)
    {
        return this.inventory.decrStackSize(this.slotIndex, amount);
    }

    public void putStack(ItemStack stack)
    {
        this.inventory.setInventorySlotContents(this.slotIndex, stack);
        this.onSlotChanged();
    }

    public boolean isEnabled()
    {
        return true;
    }

    /** Le slot peut-il prendre une stack au pickup ("returnable"). */
    public boolean canTakeStack(Object player)
    {
        return true;
    }

    public void onSlotChange(ItemStack stack, ItemStack oldStack)
    {
        this.onSlotChanged();
    }

    public void onPickupFromSlot(Object player, ItemStack stack)
    {
        this.onSlotChanged();
    }

    public ItemStack onTake(Object player, ItemStack stack)
    {
        return stack;
    }
}

/* ------------------------------------------------------------------ */
/* SlotCrafting — le slot resultat d'une grille de craft.               */
/* ------------------------------------------------------------------ */

class SlotCrafting extends Slot
{
    private final Container container;
    private final InventoryCrafting craftMatrix;

    public SlotCrafting(Container container, InventoryCrafting matrix,
                        IInventory resultInventory, int index, int x, int y)
    {
        super(resultInventory, index, x, y);
        this.container = container;
        this.craftMatrix = matrix;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return false; // pas de depot direct dans le resultat
    }

    @Override
    public ItemStack decrStackSize(int amount)
    {
        // TODO : consommer les ingredients de la grille + achievements de craft
        return super.decrStackSize(amount);
    }
}

/* ------------------------------------------------------------------ */
/* ItemStack — une pile d'items (holder minimal).                      */
/* ------------------------------------------------------------------ */

class ItemStackPlaceholder
{
    /* Voir package fr.alessdevita.valtherion.item.ItemStack :
       champs : item, stackSize, itemDamage, stackTagCompound
       methodes : copy(), areItemStacksEqual(a, b), getItem(), stackSize
       Cette classe-marque n'existe que pour rappeler l'emplacement. */
}

/* ------------------------------------------------------------------ */
/* IInventory — contrat d'inventaire logique.                          */
/* ------------------------------------------------------------------ */

interface IInventoryMarker
{
    /* Voir IInventory : getStackInSlot, decrStackSize, setInventorySlotContents,
       getInventoryStackLimit, markDirty, getSizeInventory, isItemValidForSlot. */
}
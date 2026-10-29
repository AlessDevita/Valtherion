package fr.alessdevita.valtherion.client.gui.inventory;

import java.util.List;

import com.google.common.collect.Lists;

import fr.alessdevita.valtherion.item.ItemStack;

/**
 * Container de la balise : un seul slot de paiement + inventaire joueur.
 * GuiBeacon référence ContainerBeacon et appelle les champs 0 (niveaux) et 1 (puissance primaire/secondaire encodee).
 * TODO: TileEntityBeacon, InventoryBeacon (paiement) et les effets de potions de Valtherion.
 */
public class ContainerBeacon extends Container
{
    /** L'inventaire de la balise (slot de paiement). */
    IInventory tileBeacon;
    private ContainerBeacon.BeaconSlot slotBeacon;
    private final World worldObj;
    private final BlockPos pos;
    private EntityPlayer player;

    public ContainerBeacon(InventoryPlayer playerInventory, IInventory tileBeaconIn, World worldIn, BlockPos posIn)
    {
        this.tileBeacon = tileBeaconIn;
        this.worldObj = worldIn;
        this.pos = posIn;
        this.player = playerInventory.player;
        this.addSlotToContainer(this.slotBeacon = new ContainerBeacon.BeaconSlot(tileBeaconIn, 0, 136, 110));

        for (int y = 0; y < 3; ++y)
        {
            for (int x = 0; x < 9; ++x)
            {
                this.addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 36 + x * 18, 137 + y * 18));
            }
        }

        for (int x = 0; x < 9; ++x)
        {
            this.addSlotToContainer(new Slot(playerInventory, x, 36 + x * 18, 195));
        }

        // Rattachement du listener a la balise pour les updates de champs
        this.tileBeacon.openInventory(playerInventory.player);
    }

    @Override
    public void addCraftingToCrafters(ICrafting listener)
    {
        super.addCraftingToCrafters(listener);
        listener.sendProgressBarUpdate(this, 0, this.tileBeacon.getField(0));
        listener.sendProgressBarUpdate(this, 1, this.tileBeacon.getField(1));
    }

    /** Liste des entites a portee, pour le choix des beneficiaires des effets (TODO). */
    public List getEffects()
    {
        return this.tileBeacon instanceof TileEntityBeacon
                ? ((TileEntityBeacon) this.tileBeacon).getEffects()
                : Lists.newArrayList();
    }

    @Override
    public void updateProgressBar(int id, int data)
    {
        this.tileBeacon.setField(id, data);
    }

    /** Termine la configuration de la balise (appelé par GuiBeacon sur le bouton "Terminé"). */
    @SideOnly(Side.CLIENT)
    public void updateBeacon(int primaryId, int secondaryId)
    {
        if (this.tileBeacon instanceof TileEntityBeacon)
        {
            ((TileEntityBeacon) this.tileBeacon).setPrimaryEffect(primaryId);
            ((TileEntityBeacon) this.tileBeacon).setSecondaryEffect(secondaryId);
            this.slotBeacon.decrStackSize(1);
        }

        this.tileBeacon.markDirty();
        // TODO: paquet réseau de validation C17PacketCustomPayload équivalent
    }

    /** Nombre de niveaux de la balise. Utilisé par GuiBeacon. */
    @SideOnly(Side.CLIENT)
    public int getLevels()
    {
        return this.tileBeacon.getField(0);
    }

    @Override
    public void onContainerClosed(Object playerIn)
    {
        super.onContainerClosed(playerIn);

        if (playerIn != null && !((EntityPlayer) playerIn).worldObj.isRemote)
        {
            ItemStack itemstack = this.slotBeacon.getStack();

            if (itemstack != null)
            {
                ((EntityPlayer) playerIn).dropPlayerItemWithRandomChoice(itemstack, false);
                this.slotBeacon.putStack(null);
            }
        }
    }

    @Override
    public boolean canInteractWith(Object playerIn)
    {
        return this.tileBeacon.isUseableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(Object playerIn, int index)
    {
        ItemStack itemstack = null;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 0)
            {
                if (!this.mergeItemStack(itemstack1, 1, 37, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (!this.mergeItemStack(itemstack1, 0, 1, false))
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

            slot.onPickupFromSlot(playerIn, itemstack1);
        }

        return itemstack;
    }

    /** Slot de paiement : accepte uniquement les items valides pour la balise (par defaut : diamant/emeraude/nether star). */
    class BeaconSlot extends Slot
    {
        public BeaconSlot(IInventory inventoryIn, int index, int x, int y)
        {
            super(inventoryIn, index, x, y);
        }

        public boolean isItemValid(ItemStack stack)
        {
            // TODO: adapter a l'economie d'items de Valtherion
            return stack != null
                    && (stack.getItem() == Items.diamond
                    || stack.getItem() == Items.emerald
                    || stack.getItem() == Items.nether_star);
        }

        public int getSlotStackLimit()
        {
            return 1;
        }
    }
}
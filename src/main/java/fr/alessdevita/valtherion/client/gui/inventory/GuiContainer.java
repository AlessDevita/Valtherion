package fr.alessdevita.valtherion.client.gui.inventory;

import java.util.Set;

import com.google.common.collect.Sets;

import fr.alessdevita.valtherion.client.gui.GuiScreen;
import fr.alessdevita.valtherion.item.ItemStack;

/**
 * GuiContainer — l'ecran d'inventaire generique : rend la grille de
 * slots du Container, gere les clics (pickup/depot/shift-click),
 * le drag d'items entre slots, et le rendu des items + overlays.
 * Toutes les autres GUI d'inventaire (coffre, fourneau, table...) en
 * derivent.
 */
public abstract class GuiContainer extends GuiScreen
{
    /** L'inventaire logique (cote serveur / shared). */
    public final Container inventorySlots;
    /** Largeur/hauteur du fond de GUI (texture 176x166 standard). */
    protected int xSize = 176;
    protected int ySize = 166;
    /** Coin haut-gauche du fond. */
    protected int guiLeft;
    protected int guiTop;
    /** Slot sous le curseur pendant le drag. */
    private Slot draggedSlot;
    /** Emplacement clique / l'item porte par le curseur. */
    private Slot clickedSlot;
    private boolean isRightMouseClick;
    private ItemStack draggedStack;
    /** Set des slots touches par le drag bouton gauche (nom MC : dragSplittingSlots). */
    private final Set<Slot> dragSplittingSlots = Sets.newHashSet();
    private boolean dragSplitting;
    private int dragSplittingButton;
    private int dragSplittingRemain;
    private int dragSplittingSlotNumber;

    public GuiContainer(Container container)
    {
        this.inventorySlots = container;
    }

    @Override
    public void initGui()
    {
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        this.buttonList.clear();
        this.inventorySlots.onCraftGuiOpened(this.valtherion);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        // Rendu des slots : l'item contenu + les overlays (returnable etc.)
        for (int i = 0; i < this.inventorySlots.inventorySlots.size(); ++i)
        {
            Slot slot = (Slot) this.inventorySlots.inventorySlots.get(i);

            if (slot.getHasStack())
            {
                // TODO : rendu de l'item (itemRenderer.renderItemAndEffectIntoGUI)
                // + nombre (renderItemOverlayIntoGUI) pour les stacks > 1
            }
        }

        this.drawGuiContainerForegroundLayer(mouseX, mouseY);

        // Item "porte" par le curseur (apres un pickup)
        if (this.draggedStack != null)
        {
            // TODO : rendu de l'item suivant le curseur
        }

        // Tooltip de l'item survole
        Slot hoverSlot = this.getSlotAtPosition(mouseX, mouseY);

        if (hoverSlot != null && hoverSlot.getHasStack())
        {
            // TODO : rendu du tooltip (liste des lignes de texte)
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /** Fond de la GUI (texture) — surcharge obligatoire. */
    protected abstract void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY);

    /** Foreground (labels dessines par-dessus le fond) — optionnel. */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
    }

    /** Retourne le slot a la position ecran (nom MC : getSlotAtPosition). */
    private Slot getSlotAtPosition(int mouseX, int mouseY)
    {
        for (int i = 0; i < this.inventorySlots.inventorySlots.size(); ++i)
        {
            Slot slot = (Slot) this.inventorySlots.inventorySlots.get(i);
            int slotX = this.guiLeft + slot.xDisplayPosition;
            int slotY = this.guiTop + slot.yDisplayPosition;

            if (mouseX >= slotX && mouseX < slotX + 16
                    && mouseY >= slotY && mouseY < slotY + 16 && slot.isEnabled())
            {
                return slot;
            }
        }

        return null;
    }

    /**
     * Clic dans la GUI (nom MC : handleMouseClick / mouseReleased logic).
     * @param slot le slot clique (peut etre null = hors slots)
     * @param mouseButton 0 = gauche, 1 = droit
     * @param clickType 0 = simple, 1 = shift, 2 = "clone" (molette), 6 = double-clic
     */
    protected void handleMouseClick(Slot slot, int slotId, int mouseButton, int clickType)
    {
        // TODO : deleguer au Container (inventorySlots.slotClick) selon le
        // clickType, comme MC : pickup/pickup-all/quick-move/swap/clone/drop
        this.inventorySlots.slotClick(slotId, mouseButton, clickType, this.valtherion.thePlayer);
    }

    @Override
    public void handleMouseInput(int button, int action)
    {
        super.handleMouseInput(button, action);

        if (action == org.lwjgl.glfw.GLFW.GLFW_PRESS)
        {
            // TODO : recuperer mouseX/mouseY et appeler getSlotAtPosition,
            // puis handleMouseClick avec le modificateur (shift = clickType 1)
        }
    }

    /** Fermeture : remet l'item du curseur dans l'inventaire. */
    @Override
    public void onGuiClosed()
    {
        if (this.valtherion.thePlayer != null && this.draggedStack != null)
        {
            // TODO : remettre draggedStack dans l'inventaire du joueur
            this.draggedStack = null;
        }

        this.inventorySlots.onContainerClosed(this.valtherion.thePlayer);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /** Mise a jour par tick de l'ecran (rafrachit le Container). */
    @Override
    public void updateScreen()
    {
        super.updateScreen();

        if (this.valtherion.thePlayer == null || this.valtherion.thePlayer.getHealth() <= 0.0F)
        {
            this.valtherion.displayGuiScreen(null);
        }
        else
        {
            this.inventorySlots.detectAndSendChanges();
        }
    }
}

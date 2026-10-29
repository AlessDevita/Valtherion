package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.inventory.Inventory;
import fr.alessdevita.valtherion.inventory.Item;
import fr.alessdevita.valtherion.inventory.ItemStack;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

/**
 * Interface étendue pour les conteneurs dans Valtherion.
 * Permet d'afficher un inventaire étendu et de gérer les interactions avec les slots.
 */
public class GuiListExtended extends Gui {

    // Inventaire du joueur
    private Inventory playerInventory;

    // Inventaire du conteneur
    private Inventory containerInventory;

    // Nom du conteneur
    private String containerName;

    // Bouton pour fermer l'interface
    private GuiButton closeButton;

    // Slot actuellement sélectionné
    private int selectedSlot;

    // Indique si un objet est en cours de déplacement
    private boolean isDragging;

    // Objet en cours de déplacement
    private ItemStack draggedItem;

    /**
     * Constructeur de l'interface étendue.
     * @param valtherion Instance du jeu Valtherion.
     * @param width Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     * @param playerInventory Inventaire du joueur.
     * @param containerInventory Inventaire du conteneur.
     * @param containerName Nom du conteneur.
     */
    public GuiListExtended(Valtherion valtherion, int width, int height, Inventory playerInventory, Inventory containerInventory, String containerName) {
        super(valtherion, width, height);
        this.playerInventory = playerInventory;
        this.containerInventory = containerInventory;
        this.containerName = containerName;
        this.selectedSlot = -1;
        this.isDragging = false;
        this.draggedItem = null;

        // Initialiser le bouton
        initButton();
    }

    @Override
    public void init() {
        super.init();
    }

    /**
     * Initialise le bouton de l'interface.
     */
    private void initButton() {
        float buttonWidth = 100.0f;
        float buttonHeight = 20.0f;

        // Bouton pour fermer l'interface
        closeButton = new GuiButton(
                width / 2.0f - buttonWidth / 2.0f,
                height - 30.0f,
                buttonWidth,
                buttonHeight,
                "Fermer"
        );
        closeButton.setNormalColor(new Vector4f(0.8f, 0.2f, 0.2f, 1.0f));
        closeButton.setHoverColor(new Vector4f(1.0f, 0.4f, 0.4f, 1.0f));
        closeButton.setOnClick(button -> valtherion.closeExtendedGui());

        // Ajouter le bouton à la GUI
        addElement(closeButton);
    }

    @Override
    public void render() {
        // Dessiner le fond
        drawBackground();

        // Dessiner le nom du conteneur
        drawContainerName();

        // Dessiner les slots du conteneur
        drawContainerSlots();

        // Dessiner les objets du conteneur
        drawContainerItems();

        // Dessiner les slots de l'inventaire du joueur
        drawPlayerInventorySlots();

        // Dessiner les objets de l'inventaire du joueur
        drawPlayerInventoryItems();

        // Dessiner les boutons
        super.render();
    }

    /**
     * Dessine le fond de l'interface.
     */
    protected void drawBackground() {
        GL11.glColor4f(0.1f, 0.1f, 0.1f, 0.9f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(width, 0);
        GL11.glVertex2f(width, height);
        GL11.glVertex2f(0, height);
        GL11.glEnd();
    }

    /**
     * Dessine le nom du conteneur.
     */
    private void drawContainerName() {
        // Dessiner un rectangle pour simuler le nom du conteneur
        float nameWidth = 200.0f;
        float nameHeight = 20.0f;
        float nameX = (width - nameWidth) / 2.0f;
        float nameY = height - 50.0f;

        GL11.glColor4f(1.0f, 0.8f, 0.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(nameX, nameY);
        GL11.glVertex2f(nameX + nameWidth, nameY);
        GL11.glVertex2f(nameX + nameWidth, nameY + nameHeight);
        GL11.glVertex2f(nameX, nameY + nameHeight);
        GL11.glEnd();
    }

    /**
     * Dessine les slots du conteneur.
     */
    private void drawContainerSlots() {
        int slotSize = 30;
        int slotsPerRow = 9;
        int startX = (int) ((width - slotsPerRow * slotSize) / 2.0f);
        int startY = (int) (height / 2.0f + 50.0f);

        for (int i = 0; i < containerInventory.getSize(); i++) {
            int row = i / slotsPerRow;
            int col = i % slotsPerRow;
            int x = startX + col * slotSize;
            int y = startY - row * slotSize;

            // Dessiner le slot
            GL11.glColor4f(0.3f, 0.3f, 0.3f, 1.0f);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2i(x, y);
            GL11.glVertex2i(x + slotSize, y);
            GL11.glVertex2i(x + slotSize, y + slotSize);
            GL11.glVertex2i(x, y + slotSize);
            GL11.glEnd();
        }
    }

    /**
     * Dessine les objets du conteneur.
     */
    private void drawContainerItems() {
        int slotSize = 30;
        int slotsPerRow = 9;
        int startX = (int) ((width - slotsPerRow * slotSize) / 2.0f);
        int startY = (int) (height / 2.0f + 50.0f);

        for (int i = 0; i < containerInventory.getSize(); i++) {
            Item itemStack = containerInventory.getItem(i);
            if (itemStack != null) {
                int row = i / slotsPerRow;
                int col = i % slotsPerRow;
                int x = startX + col * slotSize;
                int y = startY - row * slotSize;

                // Dessiner l'objet (simplifié)
                GL11.glColor4f(0.8f, 0.8f, 0.8f, 1.0f);
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glVertex2i(x + 2, y + 2);
                GL11.glVertex2i(x + slotSize - 2, y + 2);
                GL11.glVertex2i(x + slotSize - 2, y + slotSize - 2);
                GL11.glVertex2i(x + 2, y + slotSize - 2);
                GL11.glEnd();
            }
        }
    }

    /**
     * Dessine les slots de l'inventaire du joueur.
     */
    private void drawPlayerInventorySlots() {
        int slotSize = 30;
        int slotsPerRow = 9;
        int startX = (int) ((width - slotsPerRow * slotSize) / 2.0f);
        int startY = (int) (height / 2.0f - 50.0f);

        for (int i = 0; i < playerInventory.getSize(); i++) {
            int row = i / slotsPerRow;
            int col = i % slotsPerRow;
            int x = startX + col * slotSize;
            int y = startY - row * slotSize;

            // Dessiner le slot
            GL11.glColor4f(0.3f, 0.3f, 0.3f, 1.0f);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2i(x, y);
            GL11.glVertex2i(x + slotSize, y);
            GL11.glVertex2i(x + slotSize, y + slotSize);
            GL11.glVertex2i(x, y + slotSize);
            GL11.glEnd();
        }
    }

    /**
     * Dessine les objets de l'inventaire du joueur.
     */
    private void drawPlayerInventoryItems() {
        int slotSize = 30;
        int slotsPerRow = 9;
        int startX = (int) ((width - slotsPerRow * slotSize) / 2.0f);
        int startY = (int) (height / 2.0f - 50.0f);

        for (int i = 0; i < playerInventory.getSize(); i++) {
            Item itemStack = playerInventory.getItem(i);
            if (itemStack != null) {
                int row = i / slotsPerRow;
                int col = i % slotsPerRow;
                int x = startX + col * slotSize;
                int y = startY - row * slotSize;

                // Dessiner l'objet (simplifié)
                GL11.glColor4f(0.8f, 0.8f, 0.8f, 1.0f);
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glVertex2i(x + 2, y + 2);
                GL11.glVertex2i(x + slotSize - 2, y + 2);
                GL11.glVertex2i(x + slotSize - 2, y + slotSize - 2);
                GL11.glVertex2i(x + 2, y + slotSize - 2);
                GL11.glEnd();
            }
        }
    }

    // --- Getters et Setters ---

    public Inventory getPlayerInventory() {
        return playerInventory;
    }

    public void setPlayerInventory(Inventory playerInventory) {
        this.playerInventory = playerInventory;
    }

    public Inventory getContainerInventory() {
        return containerInventory;
    }

    public void setContainerInventory(Inventory containerInventory) {
        this.containerInventory = containerInventory;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public void setSelectedSlot(int selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    public boolean isDragging() {
        return isDragging;
    }

    public void setDragging(boolean dragging) {
        this.isDragging = dragging;
    }

    public ItemStack getDraggedItem() {
        return draggedItem;
    }

    public void setDraggedItem(ItemStack draggedItem) {
        this.draggedItem = draggedItem;
    }

    /**
     * Nettoie les ressources de l'interface.
     */
    @Override
    public void cleanup() {
        super.cleanup();
    }

    /**
     * Retourne une représentation textuelle de l'interface.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GuiExtended{" +
                "containerName='" + containerName + '\'' +
                ", selectedSlot=" + selectedSlot +
                '}';
    }
}
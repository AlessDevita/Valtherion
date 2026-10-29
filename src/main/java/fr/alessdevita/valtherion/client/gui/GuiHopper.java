package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.inventory.Inventory;
import fr.alessdevita.valtherion.inventory.Item;
import fr.alessdevita.valtherion.inventory.ItemStack;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.util.*;

/**
 * Interface de l'entonnoir (Hopper) dans Valtherion.
 * Permet d'afficher les objets dans l'entonnoir et de configurer les filtres.
 */
public class GuiHopper extends Gui {

    // Inventaire de l'entonnoir
    private Inventory hopperInventory;

    // Filtre de l'entonnoir (objets à extraire)
    private final List<String> hopperFilters;

    // Bouton pour activer/désactiver l'entonnoir
    private GuiButton toggleButton;

    // Bouton pour vider l'entonnoir
    private GuiButton clearButton;

    // Indique si l'entonnoir est actif
    private boolean isActive;

    /**
     * Constructeur de l'interface de l'entonnoir.
     * @param valtherion Instance du jeu Valtherion.
     * @param width Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     * @param hopperInventory Inventaire de l'entonnoir.
     */
    public GuiHopper(Valtherion valtherion, int width, int height, Inventory hopperInventory) {
        super(valtherion, width, height);
        this.hopperInventory = hopperInventory;
        this.hopperFilters = new ArrayList<>();
        this.isActive = true;

        // Initialiser les boutons
        initButtons();
    }

    @Override
    public void init() {
        super.init();
    }

    /**
     * Initialise les boutons de l'interface.
     */
    private void initButtons() {
        float buttonWidth = 150.0f;
        float buttonHeight = 30.0f;

        // Bouton pour activer/désactiver l'entonnoir
        toggleButton = new GuiButton(
                width / 2.0f - buttonWidth - 20.0f,
                height - 50.0f,
                buttonWidth,
                buttonHeight,
                isActive ? "Désactiver" : "Activer"
        );
        toggleButton.setNormalColor(new Vector4f(0.2f, 0.6f, 0.2f, 1.0f));
        toggleButton.setHoverColor(new Vector4f(0.4f, 0.8f, 0.4f, 1.0f));
        toggleButton.setOnClick(button -> toggleHopper());

        // Bouton pour vider l'entonnoir
        clearButton = new GuiButton(
                width / 2.0f + 20.0f,
                height - 50.0f,
                buttonWidth,
                buttonHeight,
                "Vider"
        );
        clearButton.setNormalColor(new Vector4f(0.8f, 0.2f, 0.2f, 1.0f));
        clearButton.setHoverColor(new Vector4f(1.0f, 0.4f, 0.4f, 1.0f));
        clearButton.setOnClick(button -> clearHopper());

        // Ajouter les boutons à la GUI
        addElement(toggleButton);
        addElement(clearButton);
    }

    /**
     * Active ou désactive l'entonnoir.
     */
    private void toggleHopper() {
        isActive = !isActive;
        toggleButton.setText(isActive ? "Désactiver" : "Activer");
    }

    /**
     * Vide l'entonnoir.
     */
    private void clearHopper() {
        hopperInventory.clear();
    }

    @Override
    public void render() {
        // Dessiner le fond
        drawBackground();

        // Dessiner les slots de l'inventaire
        drawInventorySlots();

        // Dessiner les objets dans l'inventaire
        drawInventoryItems();

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
     * Dessine les slots de l'inventaire.
     */
    protected void drawInventorySlots() {
        int slotSize = 30;
        int slotsPerRow = 5;
        int startX = (width - slotsPerRow * slotSize) / 2;
        int startY = height / 2 - 30;

        for (int i = 0; i < hopperInventory.getSize(); i++) {
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
     * Dessine les objets dans l'inventaire.
     */
    private void drawInventoryItems() {
        int slotSize = 30;
        int slotsPerRow = 5;
        int startX = (width - slotsPerRow * slotSize) / 2;
        int startY = height / 2 - 30;

        for (int i = 0; i < hopperInventory.getSize(); i++) {
            Item itemStack = hopperInventory.getItem(i);
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

    public Inventory getHopperInventory() {
        return hopperInventory;
    }

    public void setHopperInventory(Inventory hopperInventory) {
        this.hopperInventory = hopperInventory;
    }

    public List<String> getHopperFilters() {
        return hopperFilters;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
        toggleButton.setText(isActive ? "Désactiver" : "Activer");
    }

    /**
     * Nettoie les ressources de l'interface.
     */
    @Override
    public void cleanup() {
        super.cleanup();
        hopperFilters.clear();
    }

    /**
     * Retourne une représentation textuelle de l'interface.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GuiHopper{" +
                "hopperInventory=" + hopperInventory +
                ", isActive=" + isActive +
                '}';
    }
}

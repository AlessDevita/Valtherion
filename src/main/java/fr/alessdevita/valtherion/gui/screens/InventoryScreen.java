package fr.alessdevita.valtherion.gui.screens;

import fr.alessdevita.valtherion.gui.GUI;
import fr.alessdevita.valtherion.input.InputManager;
import fr.alessdevita.valtherion.entity.living.Player;
import fr.alessdevita.valtherion.inventory.Inventory;
import fr.alessdevita.valtherion.inventory.Item;

/**
 * Classe pour l'écran de l'inventaire dans Valtherion.
 * Affiche l'inventaire du joueur et permet de gérer les objets.
 */
public class InventoryScreen {

    // Interface graphique pour l'inventaire
    private final GUI gui;

    // Gestionnaire des entrées
    private final InputManager inputManager;

    // Joueur associé à l'inventaire
    private final Player player;

    // Inventaire du joueur
    private final Inventory inventory;

    // Slot sélectionné dans l'inventaire
    private int selectedSlot;

    // État de l'écran de l'inventaire
    private InventoryScreenState state;

    // Énumération des états de l'écran de l'inventaire
    public enum InventoryScreenState {
        VIEWING,    // Visualisation de l'inventaire
        DROPPING,   // Lâcher un objet
        USING,      // Utiliser un objet
        CRAFTING    // Mode craft
    }

    /**
     * Constructeur de l'écran de l'inventaire.
     * @param gui Interface graphique.
     * @param inputManager Gestionnaire des entrées.
     * @param player Joueur associé.
     */
    public InventoryScreen(GUI gui, InputManager inputManager, Player player) {
        this.gui = gui;
        this.inputManager = inputManager;
        this.player = player;
        this.inventory = player.getInventory();
        this.selectedSlot = -1; // Aucun slot sélectionné
        this.state = InventoryScreenState.VIEWING;
    }

    /**
     * Initialise l'écran de l'inventaire.
     */
    public void init() {
        // Initialiser les éléments de l'inventaire
        gui.setState(GUI.GUIState.INVENTORY_OPEN);
        System.out.println("Inventaire initialisé.");
    }

    /**
     * Met à jour l'écran de l'inventaire.
     */
    public void update() {
        // Mettre à jour le gestionnaire des entrées
        inputManager.update();

        // Gérer les entrées
        handleInput();
    }

    /**
     * Gère les entrées de l'écran de l'inventaire.
     */
    private void handleInput() {
        // Gérer les clics de souris
        if (inputManager.isMouseClicked()) {
            float mouseX = inputManager.getMouseX();
            float mouseY = inputManager.getMouseY();

            // Sélectionner un slot de l'inventaire
            selectedSlot = getSlotAt(mouseX, mouseY);

            if (selectedSlot != -1) {
                // Gérer l'interaction avec le slot
                handleSlotInteraction(selectedSlot);
            }
        }

        // Gérer les touches du clavier
        if (inputManager.isKeyPressed("ESCAPE")) {
            closeInventory();
        }
    }

    /**
     * Récupère le slot de l'inventaire à une position donnée.
     * @param mouseX Coordonnée X de la souris.
     * @param mouseY Coordonnée Y de la souris.
     * @return Index du slot ou -1 si aucun slot n'est trouvé.
     */
    private int getSlotAt(float mouseX, float mouseY) {
        // Logique pour déterminer quel slot de l'inventaire a été cliqué
        // Exemple : Calculer l'index du slot en fonction de (mouseX, mouseY)
        // À implémenter selon ta logique de rendu
        return -1;
    }

    /**
     * Gère l'interaction avec un slot de l'inventaire.
     * @param slot Index du slot.
     */
    private void handleSlotInteraction(int slot) {
        Item item = inventory.getItem(slot);

        if (item == null) {
            return;
        }

        switch (state) {
            case VIEWING:
                // Sélectionner le slot
                selectedSlot = slot;
                System.out.println("Slot " + slot + " sélectionné : " + item.getName());
                break;
            case DROPPING:
                // Lâcher l'objet
                dropItem(slot);
                break;
            case USING:
                // Utiliser l'objet
                useItem(slot);
                break;
            case CRAFTING:
                // Utiliser l'objet pour le craft
                craftWithItem(slot);
                break;
        }
    }

    /**
     * Lâche un objet de l'inventaire.
     * @param slot Index du slot.
     */
    private void dropItem(int slot) {
        Item item = inventory.getItem(slot);
        if (item != null) {
            // Logique pour lâcher l'objet dans le monde
            // Exemple : player.getWorld().dropItem(item.getId(), player.getX(), player.getY(), player.getZ(), 1);
            inventory.removeItem(item, 1);
            System.out.println("Lâcher de " + item.getName() + " depuis le slot " + slot);
        }
    }

    /**
     * Utilise un objet de l'inventaire.
     * @param slot Index du slot.
     */
    private void useItem(int slot) {
        Item item = inventory.getItem(slot);
        if (item != null) {
            player.useItem(item);
            System.out.println("Utilisation de " + item.getName() + " depuis le slot " + slot);
        }
    }

    /**
     * Utilise un objet pour le craft.
     * @param slot Index du slot.
     */
    private void craftWithItem(int slot) {
        Item item = inventory.getItem(slot);
        if (item != null) {
            // Logique pour le craft avec l'objet
            System.out.println("Craft avec " + item.getName() + " depuis le slot " + slot);
        }
    }

    /**
     * Rend l'écran de l'inventaire.
     */
    public void render() {
        // Rendre l'inventaire
        gui.render();
        System.out.println("Rendu de l'inventaire.");
    }

    /**
     * Ferme l'inventaire.
     */
    public void closeInventory() {
        System.out.println("Fermeture de l'inventaire.");
        gui.setState(GUI.GUIState.IN_GAME);
    }

    /**
     * Définit l'état de l'écran de l'inventaire.
     * @param state État à définir.
     */
    public void setState(InventoryScreenState state) {
        this.state = state;
    }

    // --- Getters ---

    public InventoryScreenState getState() {
        return state;
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
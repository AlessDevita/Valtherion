package fr.alessdevita.valtherion.gui;

import fr.alessdevita.valtherion.entity.living.Player;
import fr.alessdevita.valtherion.inventory.Inventory;
import fr.alessdevita.valtherion.world.World;

import java.util.*;

/**
 * Classe de base pour l'interface graphique (GUI) de Valtherion.
 * Gère l'affichage des menus, inventaires, barres de vie/expérience, et autres éléments UI.
 */
public class GUI {

    // Joueur associé à cette GUI
    private Player player;

    // Monde associé à cette GUI
    private World world;

    // Liste des éléments GUI à afficher
    private final List<GUIElement> elements;

    // État actuel de la GUI (ex: IN_GAME, INVENTORY_OPEN, PAUSED, etc.)
    private GUIState currentState;

    public void init() {

    }

    public void cleanup() {

    }

    // Énumération des états de la GUI
    public enum GUIState {
        IN_GAME,          // En jeu
        INVENTORY_OPEN,   // Inventaire ouvert
        PAUSED,           // Jeu en pause
        MENU,             // Menu principal
        SETTINGS,         // Paramètres
        CRAFTING,         // Table de craft
        TRADE,            // Fenêtre de commerce
        QUEST_LOG         // Journal des quêtes
    }

    // Classe de base pour les éléments GUI
    public static abstract class GUIElement {
        protected float x, y; // Position de l'élément
        protected float width, height; // Taille de l'élément
        protected boolean isVisible; // Visibilité de l'élément

        public GUIElement(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.isVisible = true;
        }

        // Méthode pour rendre l'élément
        public abstract void render();

        // Méthode pour mettre à jour l'élément
        public abstract void update();

        // Méthode pour gérer les clics sur l'élément
        public abstract void onClick(float mouseX, float mouseY);

        // Getters et Setters
        public float getX() { return x; }
        public void setX(float x) { this.x = x; }
        public float getY() { return y; }
        public void setY(float y) { this.y = y; }
        public float getWidth() { return width; }
        public void setWidth(float width) { this.width = width; }
        public float getHeight() { return height; }
        public void setHeight(float height) { this.height = height; }
        public boolean isVisible() { return isVisible; }
        public void setVisible(boolean visible) { isVisible = visible; }

        // Vérifie si le point (mouseX, mouseY) est dans l'élément
        public boolean contains(float mouseX, float mouseY) {
            return mouseX >= x && mouseX <= x + width &&
                    mouseY >= y && mouseY <= y + height;
        }
    }

    /**
     * Constructeur de la GUI.
     * @param player Joueur associé à cette GUI.
     * @param world Monde associé à cette GUI.
     */
    public GUI(Player player, World world) {
        this.player = player;
        this.world = world;
        this.elements = new ArrayList<>();
        this.currentState = GUIState.IN_GAME;

        // Initialiser les éléments GUI de base
        initDefaultElements();
    }

    /**
     * Initialise les éléments GUI par défaut.
     */
    private void initDefaultElements() {
        // Ajouter la barre de vie
        elements.add(new HealthBar(10, 10, 200, 20));

        // Ajouter la barre d'expérience
        elements.add(new ExperienceBar(10, 40, 200, 10));

        // Ajouter la barre de nourriture (optionnelle)
        elements.add(new HungerBar(10, 60, 200, 10));

        // Ajouter l'affichage des FPS
        elements.add(new FPSCounter(10, 80, 100, 20));
    }

    /**
     * Met à jour la GUI.
     * Appelé à chaque tick du jeu.
     */
    public void update() {
        // Mettre à jour tous les éléments GUI
        for (GUIElement element : elements) {
            if (element.isVisible()) {
                element.update();
            }
        }
    }

    /**
     * Rend la GUI.
     */
    public void render() {
        // Rendre tous les éléments GUI
        for (GUIElement element : elements) {
            if (element.isVisible()) {
                element.render();
            }
        }
    }

    /**
     * Gère un clic de souris.
     * @param mouseX Coordonnée X de la souris.
     * @param mouseY Coordonnée Y de la souris.
     */
    public void handleClick(float mouseX, float mouseY) {
        // Parcourir les éléments GUI dans l'ordre inverse pour gérer les superpositions
        for (int i = elements.size() - 1; i >= 0; i--) {
            GUIElement element = elements.get(i);
            if (element.isVisible() && element.contains(mouseX, mouseY)) {
                element.onClick(mouseX, mouseY);
                break; // Un seul élément peut être cliqué à la fois
            }
        }
    }

    /**
     * Ajoute un élément GUI.
     * @param element Élément à ajouter.
     */
    public void addElement(GUIElement element) {
        elements.add(element);
    }

    /**
     * Retire un élément GUI.
     * @param element Élément à retirer.
     */
    public void removeElement(GUIElement element) {
        elements.remove(element);
    }

    /**
     * Définit l'état de la GUI.
     * @param state État à définir.
     */
    public void setState(GUIState state) {
        this.currentState = state;

        // Mettre à jour la visibilité des éléments en fonction de l'état
        updateElementsVisibility();
    }

    /**
     * Met à jour la visibilité des éléments en fonction de l'état actuel.
     */
    private void updateElementsVisibility() {
        for (GUIElement element : elements) {
            switch (currentState) {
                case IN_GAME:
                    // Afficher les barres de vie, expérience, nourriture, et FPS
                    element.setVisible(
                            element instanceof HealthBar ||
                                    element instanceof ExperienceBar ||
                                    element instanceof HungerBar ||
                                    element instanceof FPSCounter
                    );
                    break;
                case INVENTORY_OPEN:
                    // Afficher l'inventaire et masquer les autres éléments
                    element.setVisible(element instanceof InventoryGUI);
                    break;
                case PAUSED:
                    // Afficher le menu de pause
                    element.setVisible(element instanceof PauseMenu);
                    break;
                case MENU:
                    // Afficher le menu principal
                    element.setVisible(element instanceof MainMenu);
                    break;
                default:
                    element.setVisible(true);
                    break;
            }
        }
    }

    /**
     * Ouvre l'inventaire du joueur.
     */
    public void openInventory() {
        // Créer un nouvel élément GUI pour l'inventaire
        InventoryGUI inventoryGUI = new InventoryGUI(100, 100, 400, 300, player.getInventory());
        addElement(inventoryGUI);

        // Changer l'état de la GUI
        setState(GUIState.INVENTORY_OPEN);
    }

    /**
     * Ferme l'inventaire du joueur.
     */
    public void closeInventory() {
        // Retirer l'élément GUI de l'inventaire
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i) instanceof InventoryGUI) {
                elements.remove(i);
                break;
            }
        }

        // Revenir à l'état IN_GAME
        setState(GUIState.IN_GAME);
    }

    // --- Classes internes pour les éléments GUI ---

    /**
     * Barre de vie du joueur.
     */
    private class HealthBar extends GUIElement {
        public HealthBar(float x, float y, float width, float height) {
            super(x, y, width, height);
        }

        @Override
        public void render() {
            // Logique pour rendre la barre de vie
            // Exemple : Dessiner une barre rouge avec un pourcentage de vie
            float healthPercentage = player.getHealth() / player.getMaxHealth();
            System.out.println("Rendu de la barre de vie : " + (healthPercentage * 100) + "%");
        }

        @Override
        public void update() {
            // Pas de mise à jour nécessaire pour la barre de vie
        }

        @Override
        public void onClick(float mouseX, float mouseY) {
            // Pas d'action pour un clic sur la barre de vie
        }
    }

    /**
     * Barre d'expérience du joueur.
     */
    private class ExperienceBar extends GUIElement {
        public ExperienceBar(float x, float y, float width, float height) {
            super(x, y, width, height);
        }

        @Override
        public void render() {
            // Logique pour rendre la barre d'expérience
            float experiencePercentage = (float) player.getExperience() / player.getExperienceToNextLevel();
            System.out.println("Rendu de la barre d'expérience : " + (experiencePercentage * 100) + "%");
        }

        @Override
        public void update() {
            // Pas de mise à jour nécessaire pour la barre d'expérience
        }

        @Override
        public void onClick(float mouseX, float mouseY) {
            // Pas d'action pour un clic sur la barre d'expérience
        }
    }

    /**
     * Barre de nourriture du joueur.
     */
    private class HungerBar extends GUIElement {
        public HungerBar(float x, float y, float width, float height) {
            super(x, y, width, height);
        }

        @Override
        public void render() {
            // Logique pour rendre la barre de nourriture
            System.out.println("Rendu de la barre de nourriture");
        }

        @Override
        public void update() {
            // Pas de mise à jour nécessaire pour la barre de nourriture
        }

        @Override
        public void onClick(float mouseX, float mouseY) {
            // Pas d'action pour un clic sur la barre de nourriture
        }
    }

    /**
     * Compteur de FPS.
     */
    private class FPSCounter extends GUIElement {
        private int fps;

        public FPSCounter(float x, float y, float width, float height) {
            super(x, y, width, height);
            this.fps = 0;
        }

        public void setFPS(int fps) {
            this.fps = fps;
        }

        @Override
        public void render() {
            // Logique pour rendre le compteur de FPS
            System.out.println("Rendu du compteur de FPS : " + fps);
        }

        @Override
        public void update() {
            // Pas de mise à jour nécessaire pour le compteur de FPS
        }

        @Override
        public void onClick(float mouseX, float mouseY) {
            // Pas d'action pour un clic sur le compteur de FPS
        }
    }

    /**
     * GUI pour l'inventaire du joueur.
     */
    private class InventoryGUI extends GUIElement {
        private Inventory inventory;

        public InventoryGUI(float x, float y, float width, float height, Inventory inventory) {
            super(x, y, width, height);
            this.inventory = inventory;
        }

        @Override
        public void render() {
            // Logique pour rendre l'inventaire
            System.out.println("Rendu de l'inventaire avec " + inventory.getSize() + " slots");
        }

        @Override
        public void update() {
            // Pas de mise à jour nécessaire pour l'inventaire
        }

        @Override
        public void onClick(float mouseX, float mouseY) {
            // Gérer les clics sur les slots de l'inventaire
            System.out.println("Clic sur l'inventaire à (" + mouseX + ", " + mouseY + ")");
        }
    }

    /**
     * Menu de pause.
     */
    private class PauseMenu extends GUIElement {
        public PauseMenu(float x, float y, float width, float height) {
            super(x, y, width, height);
        }

        @Override
        public void render() {
            // Logique pour rendre le menu de pause
            System.out.println("Rendu du menu de pause");
        }

        @Override
        public void update() {
            // Pas de mise à jour nécessaire pour le menu de pause
        }

        @Override
        public void onClick(float mouseX, float mouseY) {
            // Gérer les clics sur les boutons du menu de pause
            System.out.println("Clic sur le menu de pause à (" + mouseX + ", " + mouseY + ")");
        }
    }

    /**
     * Menu principal.
     */
    private class MainMenu extends GUIElement {
        public MainMenu(float x, float y, float width, float height) {
            super(x, y, width, height);
        }

        @Override
        public void render() {
            // Logique pour rendre le menu principal
            System.out.println("Rendu du menu principal");
        }

        @Override
        public void update() {
            // Pas de mise à jour nécessaire pour le menu principal
        }

        @Override
        public void onClick(float mouseX, float mouseY) {
            // Gérer les clics sur les boutons du menu principal
            System.out.println("Clic sur le menu principal à (" + mouseX + ", " + mouseY + ")");
        }
    }

    // --- Getters et Setters ---

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public GUIState getCurrentState() {
        return currentState;
    }

    public List<GUIElement> getElements() {
        return elements;
    }
}
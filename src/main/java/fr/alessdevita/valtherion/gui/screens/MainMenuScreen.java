package fr.alessdevita.valtherion.gui.screens;

import fr.alessdevita.valtherion.gui.GUI;
import fr.alessdevita.valtherion.input.InputManager;

/**
 * Classe pour l'écran du menu principal dans Valtherion.
 * Affiche les options pour démarrer une nouvelle partie, charger une partie, ou quitter le jeu.
 */
public class MainMenuScreen {

    // Interface graphique pour le menu principal
    private GUI gui = null;

    // Gestionnaire des entrées
    private InputManager inputManager = null;

    // État de l'écran du menu principal
    private MainMenuState state;

    // Énumération des états du menu principal
    public enum MainMenuState {
        MAIN,       // Menu principal
        NEW_GAME,   // Nouveau jeu
        LOAD_GAME,  // Charger une partie
        SETTINGS,   // Paramètres
        QUIT        // Quitter
    }

    /**
     * Constructeur de l'écran du menu principal.
     */
    public MainMenuScreen() {
        this.gui = gui;
        this.inputManager = inputManager;
        this.state = MainMenuState.MAIN;
    }

    /**
     * Initialise l'écran du menu principal.
     */
    public void init() {
        // Initialiser les éléments du menu principal
        gui.setState(GUI.GUIState.MENU);
        System.out.println("Menu principal initialisé.");
    }

    /**
     * Met à jour l'écran du menu principal.
     */
    public void update() {
        // Mettre à jour le gestionnaire des entrées
        inputManager.update();

        // Gérer les entrées
        handleInput();
    }

    /**
     * Gère les entrées du menu principal.
     */
    private void handleInput() {
        // Gérer les clics de souris
        if (inputManager.isMouseClicked()) {
            float mouseX = inputManager.getMouseX();
            float mouseY = inputManager.getMouseY();

            // Vérifier quel bouton a été cliqué
            if (isButtonClicked(mouseX, mouseY, "NEW_GAME")) {
                state = MainMenuState.NEW_GAME;
                System.out.println("Nouveau jeu sélectionné.");
            } else if (isButtonClicked(mouseX, mouseY, "LOAD_GAME")) {
                state = MainMenuState.LOAD_GAME;
                System.out.println("Charger une partie sélectionné.");
            } else if (isButtonClicked(mouseX, mouseY, "SETTINGS")) {
                state = MainMenuState.SETTINGS;
                System.out.println("Paramètres sélectionnés.");
            } else if (isButtonClicked(mouseX, mouseY, "QUIT")) {
                state = MainMenuState.QUIT;
                System.out.println("Quitter sélectionné.");
            }
        }

        // Gérer les touches du clavier
        if (inputManager.isKeyPressed("ESCAPE")) {
            state = MainMenuState.MAIN;
            System.out.println("Retour au menu principal.");
        }
    }

    /**
     * Vérifie si un bouton a été cliqué.
     * @param mouseX Coordonnée X de la souris.
     * @param mouseY Coordonnée Y de la souris.
     * @param buttonName Nom du bouton.
     * @return true si le bouton a été cliqué, false sinon.
     */
    private boolean isButtonClicked(float mouseX, float mouseY, String buttonName) {
        // Logique pour vérifier si le bouton a été cliqué
        // Exemple : Vérifier si (mouseX, mouseY) est dans la zone du bouton
        // À implémenter selon ta logique de rendu
        return false;
    }

    /**
     * Rend l'écran du menu principal.
     */
    public void render() {
        // Rendre le menu principal
        gui.render();
        System.out.println("Rendu du menu principal.");
    }

    /**
     * Démarre une nouvelle partie.
     */
    public void startNewGame() {
        System.out.println("Démarrage d'une nouvelle partie.");
        // Logique pour démarrer une nouvelle partie
    }

    /**
     * Charge une partie sauvegardée.
     */
    public void loadGame() {
        System.out.println("Chargement d'une partie sauvegardée.");
        // Logique pour charger une partie
    }

    /**
     * Ouvre les paramètres.
     */
    public void openSettings() {
        System.out.println("Ouverture des paramètres.");
        // Logique pour ouvrir les paramètres
    }

    /**
     * Quitte le jeu.
     */
    public void quitGame() {
        System.out.println("Fermeture du jeu.");
        // Logique pour quitter le jeu
    }

    // --- Getters et Setters ---

    public MainMenuState getState() {
        return state;
    }

    public void setState(MainMenuState state) {
        this.state = state;
    }
}
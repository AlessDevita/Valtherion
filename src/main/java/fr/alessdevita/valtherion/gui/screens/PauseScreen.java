package fr.alessdevita.valtherion.gui.screens;

import fr.alessdevita.valtherion.gui.GUI;
import fr.alessdevita.valtherion.input.InputManager;

/**
 * Classe pour l'écran de pause dans Valtherion.
 * Affiche les options pour reprendre, sauvegarder, charger, ou quitter la partie.
 */
public class PauseScreen {

    // Interface graphique pour le menu de pause
    private final GUI gui;

    // Gestionnaire des entrées
    private final InputManager inputManager;

    // État de l'écran de pause
    private PauseScreenState state;

    // Énumération des états de l'écran de pause
    public enum PauseScreenState {
        PAUSED,      // En pause
        RESUME,      // Reprendre
        SAVE_GAME,  // Sauvegarder
        LOAD_GAME,  // Charger
        SETTINGS,   // Paramètres
        QUIT        // Quitter
    }

    /**
     * Constructeur de l'écran de pause.
     * @param gui Interface graphique.
     * @param inputManager Gestionnaire des entrées.
     */
    public PauseScreen(GUI gui, InputManager inputManager) {
        this.gui = gui;
        this.inputManager = inputManager;
        this.state = PauseScreenState.PAUSED;
    }

    /**
     * Initialise l'écran de pause.
     */
    public void init() {
        // Initialiser les éléments du menu de pause
        gui.setState(GUI.GUIState.PAUSED);
        System.out.println("Menu de pause initialisé.");
    }

    /**
     * Met à jour l'écran de pause.
     */
    public void update() {
        // Mettre à jour le gestionnaire des entrées
        inputManager.update();

        // Gérer les entrées
        handleInput();
    }

    /**
     * Gère les entrées de l'écran de pause.
     */
    private void handleInput() {
        // Gérer les clics de souris
        if (inputManager.isMouseClicked()) {
            float mouseX = inputManager.getMouseX();
            float mouseY = inputManager.getMouseY();

            // Vérifier quel bouton a été cliqué
            if (isButtonClicked(mouseX, mouseY, "RESUME")) {
                state = PauseScreenState.RESUME;
                System.out.println("Reprise de la partie.");
            } else if (isButtonClicked(mouseX, mouseY, "SAVE_GAME")) {
                state = PauseScreenState.SAVE_GAME;
                System.out.println("Sauvegarde de la partie.");
            } else if (isButtonClicked(mouseX, mouseY, "LOAD_GAME")) {
                state = PauseScreenState.LOAD_GAME;
                System.out.println("Chargement d'une partie.");
            } else if (isButtonClicked(mouseX, mouseY, "SETTINGS")) {
                state = PauseScreenState.SETTINGS;
                System.out.println("Ouverture des paramètres.");
            } else if (isButtonClicked(mouseX, mouseY, "QUIT")) {
                state = PauseScreenState.QUIT;
                System.out.println("Quitter la partie.");
            }
        }

        // Gérer les touches du clavier
        if (inputManager.isKeyPressed("ESCAPE")) {
            state = PauseScreenState.RESUME;
            System.out.println("Retour à la partie.");
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
     * Rend l'écran de pause.
     */
    public void render() {
        // Rendre le menu de pause
        gui.render();
        System.out.println("Rendu du menu de pause.");
    }

    /**
     * Reprend la partie.
     */
    public void resumeGame() {
        System.out.println("Reprise de la partie.");
        // Logique pour reprendre la partie
    }

    /**
     * Sauvegarde la partie.
     */
    public void saveGame() {
        System.out.println("Sauvegarde de la partie.");
        // Logique pour sauvegarder la partie
    }

    /**
     * Charge une partie.
     */
    public void loadGame() {
        System.out.println("Chargement d'une partie.");
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
     * Quitte la partie.
     */
    public void quitGame() {
        System.out.println("Quitter la partie.");
        // Logique pour quitter la partie
    }

    // --- Getters et Setters ---

    public PauseScreenState getState() {
        return state;
    }

    public void setState(PauseScreenState state) {
        this.state = state;
    }
}
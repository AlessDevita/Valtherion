package fr.alessdevita.valtherion.gui.screens;

import fr.alessdevita.valtherion.gui.GUI;
import fr.alessdevita.valtherion.input.InputManager;
import fr.alessdevita.valtherion.core.Configuration;

/**
 * Classe pour l'écran des paramètres dans Valtherion.
 * Permet de modifier les paramètres du jeu comme le volume, les contrôles, et les options graphiques.
 */
public class SettingsScreen {

    // Interface graphique pour les paramètres
    private final GUI gui;

    // Gestionnaire des entrées
    private final InputManager inputManager;

    // Configuration du jeu
    private final Configuration configuration;

    // État de l'écran des paramètres
    private SettingsScreenState state;

    // Index du paramètre sélectionné
    private int selectedSettingIndex;

    // Énumération des états de l'écran des paramètres
    public enum SettingsScreenState {
        MAIN,           // Menu principal des paramètres
        AUDIO,          // Paramètres audio
        CONTROLS,       // Paramètres des contrôles
        GRAPHICS,       // Paramètres graphiques
        BACK            // Retour
    }

    // Énumération des paramètres disponibles
    public enum SettingType {
        MASTER_VOLUME,   // Volume principal
        MUSIC_VOLUME,    // Volume de la musique
        SOUND_VOLUME,    // Volume des sons
        FULLSCREEN,      // Mode plein écran
        VSYNC,           // Synchronisation verticale
        SENSITIVITY,      // Sensibilité de la souris
        RESOLUTION       // Résolution
    }

    /**
     * Constructeur de l'écran des paramètres.
     * @param gui Interface graphique.
     * @param inputManager Gestionnaire des entrées.
     * @param configuration Configuration du jeu.
     */
    public SettingsScreen(GUI gui, InputManager inputManager, Configuration configuration) {
        this.gui = gui;
        this.inputManager = inputManager;
        this.configuration = configuration;
        this.state = SettingsScreenState.MAIN;
        this.selectedSettingIndex = 0;
    }

    /**
     * Initialise l'écran des paramètres.
     */
    public void init() {
        // Initialiser les éléments des paramètres
        gui.setState(GUI.GUIState.SETTINGS);
        System.out.println("Paramètres initialisés.");
    }

    /**
     * Met à jour l'écran des paramètres.
     */
    public void update() {
        // Mettre à jour le gestionnaire des entrées
        inputManager.update();

        // Gérer les entrées
        handleInput();
    }

    /**
     * Gère les entrées de l'écran des paramètres.
     */
    private void handleInput() {
        // Gérer les clics de souris
        if (inputManager.isMouseClicked()) {
            float mouseX = inputManager.getMouseX();
            float mouseY = inputManager.getMouseY();

            // Vérifier quel paramètre a été sélectionné
            selectedSettingIndex = getSettingIndexAt(mouseX, mouseY);

            if (selectedSettingIndex != -1) {
                handleSettingSelection(selectedSettingIndex);
            }
        }

        // Gérer les touches du clavier
        if (inputManager.isKeyPressed("ESCAPE")) {
            state = SettingsScreenState.BACK;
            System.out.println("Retour au menu précédent.");
        }
    }

    /**
     * Récupère l'index du paramètre à une position donnée.
     * @param mouseX Coordonnée X de la souris.
     * @param mouseY Coordonnée Y de la souris.
     * @return Index du paramètre ou -1 si aucun paramètre n'est trouvé.
     */
    private int getSettingIndexAt(float mouseX, float mouseY) {
        // Logique pour déterminer quel paramètre a été cliqué
        // Exemple : Calculer l'index en fonction de (mouseX, mouseY)
        // À implémenter selon ta logique de rendu
        return -1;
    }

    /**
     * Gère la sélection d'un paramètre.
     * @param settingIndex Index du paramètre.
     */
    private void handleSettingSelection(int settingIndex) {
        SettingType setting = SettingType.values()[settingIndex];

        switch (setting) {
            case MASTER_VOLUME:
                adjustMasterVolume();
                break;
            case MUSIC_VOLUME:
                adjustMusicVolume();
                break;
            case SOUND_VOLUME:
                adjustSoundVolume();
                break;
            case FULLSCREEN:
                toggleFullscreen();
                break;
            case VSYNC:
                toggleVsync();
                break;
            case SENSITIVITY:
                adjustSensitivity();
                break;
            case RESOLUTION:
                adjustResolution();
                break;
        }
    }

    /**
     * Ajuste le volume principal.
     */
    private void adjustMasterVolume() {
        float currentVolume = configuration.getMasterVolume();
        float newVolume = currentVolume + 0.1f;
        if (newVolume > 1.0f) {
            newVolume = 0.0f;
        }
        configuration.setMasterVolume(newVolume);
        System.out.println("Volume principal défini à : " + newVolume);
    }

    /**
     * Ajuste le volume de la musique.
     */
    private void adjustMusicVolume() {
        float currentVolume = configuration.getMusicVolume();
        float newVolume = currentVolume + 0.1f;
        if (newVolume > 1.0f) {
            newVolume = 0.0f;
        }
        configuration.setMusicVolume(newVolume);
        System.out.println("Volume de la musique défini à : " + newVolume);
    }

    /**
     * Ajuste le volume des sons.
     */
    private void adjustSoundVolume() {
        float currentVolume = configuration.getSoundVolume();
        float newVolume = currentVolume + 0.1f;
        if (newVolume > 1.0f) {
            newVolume = 0.0f;
        }
        configuration.setSoundVolume(newVolume);
        System.out.println("Volume des sons défini à : " + newVolume);
    }

    /**
     * Active ou désactive le mode plein écran.
     */
    private void toggleFullscreen() {
        boolean isFullscreen = configuration.isFullscreen();
        configuration.setFullscreen(!isFullscreen);
        System.out.println("Mode plein écran : " + (!isFullscreen ? "activé" : "désactivé"));
    }

    /**
     * Active ou désactive la synchronisation verticale.
     */
    private void toggleVsync() {
        boolean isVsync = configuration.isVsync();
        configuration.setVsync(!isVsync);
        System.out.println("Vsync : " + (!isVsync ? "activé" : "désactivé"));
    }

    /**
     * Ajuste la sensibilité de la souris.
     */
    private void adjustSensitivity() {
        // Logique pour ajuster la sensibilité de la souris
        System.out.println("Sensibilité de la souris ajustée.");
    }

    /**
     * Ajuste la résolution.
     */
    private void adjustResolution() {
        // Logique pour ajuster la résolution
        System.out.println("Résolution ajustée.");
    }

    /**
     * Rend l'écran des paramètres.
     */
    public void render() {
        // Rendre les paramètres
        gui.render();
        System.out.println("Rendu des paramètres.");
    }

    /**
     * Sauvegarde les paramètres.
     */
    public void saveSettings() {
        System.out.println("Paramètres sauvegardés.");
        // Logique pour sauvegarder les paramètres dans un fichier
    }

    /**
     * Retourne au menu précédent.
     */
    public void goBack() {
        System.out.println("Retour au menu précédent.");
        gui.setState(GUI.GUIState.MENU);
    }

    // --- Getters et Setters ---

    public SettingsScreenState getState() {
        return state;
    }

    public void setState(SettingsScreenState state) {
        this.state = state;
    }

    public int getSelectedSettingIndex() {
        return selectedSettingIndex;
    }
}
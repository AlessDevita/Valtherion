package fr.alessdevita.valtherion.input;

import java.util.*;

/**
 * Classe pour gérer les entrées utilisateur dans Valtherion.
 * Gère les événements du clavier, de la souris, et des manettes.
 */
public class InputManager {

    // État des touches du clavier
    private final Map<String, Boolean> keyStates;

    // État des boutons de la souris
    private final Map<Integer, Boolean> mouseButtonStates;

    // Position de la souris
    private float mouseX, mouseY;

    // Décalage de la souris (pour le mode FPS)
    private float mouseDX, mouseDY;

    // État des boutons de la souris (cliqué ou non)
    private boolean mouseClicked;

    // Liste des observateurs d'entrées
    private final List<InputListener> inputListeners;

    // Liste des actions associées aux touches
    private final Map<String, InputAction> keyActions;

    // Liste des actions associées aux boutons de la souris
    private final Map<Integer, InputAction> mouseActions;

    /**
     * Constructeur du gestionnaire des entrées.
     */
    public InputManager() {
        this.keyStates = new HashMap<>();
        this.mouseButtonStates = new HashMap<>();
        this.mouseX = 0.0f;
        this.mouseY = 0.0f;
        this.mouseDX = 0.0f;
        this.mouseDY = 0.0f;
        this.mouseClicked = false;
        this.inputListeners = new ArrayList<>();
        this.keyActions = new HashMap<>();
        this.mouseActions = new HashMap<>();
    }

    /**
     * Initialise le gestionnaire des entrées.
     */
    public void init() {
        // Initialiser les états des touches
        // Exemple : Ajouter des touches par défaut
        registerKey("W");
        registerKey("A");
        registerKey("S");
        registerKey("D");
        registerKey("SPACE");
        registerKey("LEFT_SHIFT");
        registerKey("ESCAPE");
        registerKey("E");

        // Initialiser les états des boutons de la souris
        registerMouseButton(0); // Bouton gauche
        registerMouseButton(1); // Bouton droit
        registerMouseButton(2); // Bouton du milieu

        System.out.println("Gestionnaire des entrées initialisé.");
    }

    /**
     * Enregistre une touche.
     * @param key Nom de la touche.
     */
    public void registerKey(String key) {
        keyStates.put(key, false);
    }

    /**
     * Enregistre un bouton de la souris.
     * @param button Code du bouton.
     */
    public void registerMouseButton(int button) {
        mouseButtonStates.put(button, false);
    }

    /**
     * Met à jour le gestionnaire des entrées.
     */
    public void update() {
        // Réinitialiser les états des touches et des boutons
        mouseClicked = false;
        mouseDX = 0.0f;
        mouseDY = 0.0f;

        // Notifier les observateurs
        for (InputListener listener : inputListeners) {
            listener.onInputUpdate(this);
        }
    }

    /**
     * Appelé lorsque une touche est pressée.
     * @param key Nom de la touche.
     */
    public void onKeyPress(String key) {
        keyStates.put(key, true);

        // Exécuter l'action associée à la touche
        InputAction action = keyActions.get(key);
        if (action != null) {
            action.execute();
        }

        // Notifier les observateurs
        for (InputListener listener : inputListeners) {
            listener.onKeyPress(key);
        }
    }

    /**
     * Appelé lorsque une touche est relâchée.
     * @param key Nom de la touche.
     */
    public void onKeyRelease(String key) {
        keyStates.put(key, false);

        // Notifier les observateurs
        for (InputListener listener : inputListeners) {
            listener.onKeyRelease(key);
        }
    }

    /**
     * Appelé lorsque un bouton de la souris est pressé.
     * @param button Code du bouton.
     */
    public void onMousePress(int button) {
        mouseButtonStates.put(button, true);
        mouseClicked = true;

        // Exécuter l'action associée au bouton
        InputAction action = mouseActions.get(button);
        if (action != null) {
            action.execute();
        }

        // Notifier les observateurs
        for (InputListener listener : inputListeners) {
            listener.onMousePress(button);
        }
    }

    /**
     * Appelé lorsque un bouton de la souris est relâché.
     * @param button Code du bouton.
     */
    public void onMouseRelease(int button) {
        mouseButtonStates.put(button, false);

        // Notifier les observateurs
        for (InputListener listener : inputListeners) {
            listener.onMouseRelease(button);
        }
    }

    /**
     * Appelé lorsque la souris est déplacée.
     * @param x Coordonnée X de la souris.
     * @param y Coordonnée Y de la souris.
     */
    public void onMouseMove(float x, float y) {
        mouseDX = x - mouseX;
        mouseDY = y - mouseY;
        mouseX = x;
        mouseY = y;

        // Notifier les observateurs
        for (InputListener listener : inputListeners) {
            listener.onMouseMove(mouseX, mouseY);
        }
    }

    /**
     * Appelé lorsque la molette de la souris est utilisée.
     * @param deltaX Décalage horizontal de la molette.
     * @param deltaY Décalage vertical de la molette.
     */
    public void onMouseScroll(float deltaX, float deltaY) {
        // Notifier les observateurs
        for (InputListener listener : inputListeners) {
            listener.onMouseScroll(deltaX, deltaY);
        }
    }

    /**
     * Vérifie si une touche est enfoncée.
     * @param key Nom de la touche.
     * @return true si la touche est enfoncée, false sinon.
     */
    public boolean isKeyDown(String key) {
        return keyStates.getOrDefault(key, false);
    }

    /**
     * Vérifie si une touche a été pressée (une seule fois).
     * @param key Nom de la touche.
     * @return true si la touche a été pressée, false sinon.
     */
    public boolean isKeyPressed(String key) {
        // À implémenter selon ta logique de gestion des entrées
        // Exemple : Vérifier si la touche a été pressée dans le dernier frame
        return false;
    }

    /**
     * Vérifie si un bouton de la souris est enfoncé.
     * @param button Code du bouton.
     * @return true si le bouton est enfoncé, false sinon.
     */
    public boolean isMouseButtonDown(int button) {
        return mouseButtonStates.getOrDefault(button, false);
    }

    /**
     * Vérifie si un bouton de la souris a été cliqué.
     * @return true si un bouton a été cliqué, false sinon.
     */
    public boolean isMouseClicked() {
        return mouseClicked;
    }

    /**
     * Récupère la position X de la souris.
     * @return Coordonnée X de la souris.
     */
    public float getMouseX() {
        return mouseX;
    }

    /**
     * Récupère la position Y de la souris.
     * @return Coordonnée Y de la souris.
     */
    public float getMouseY() {
        return mouseY;
    }

    /**
     * Récupère le décalage X de la souris.
     * @return Décalage X de la souris.
     */
    public float getMouseDX() {
        return mouseDX;
    }

    /**
     * Récupère le décalage Y de la souris.
     * @return Décalage Y de la souris.
     */
    public float getMouseDY() {
        return mouseDY;
    }

    /**
     * Ajoute un observateur d'entrées.
     * @param listener Observateur à ajouter.
     */
    public void addInputListener(InputListener listener) {
        inputListeners.add(listener);
    }

    /**
     * Retire un observateur d'entrées.
     * @param listener Observateur à retirer.
     */
    public void removeInputListener(InputListener listener) {
        inputListeners.remove(listener);
    }

    /**
     * Associe une action à une touche.
     * @param key Nom de la touche.
     * @param action Action à associer.
     */
    public void bindKey(String key, InputAction action) {
        keyActions.put(key, action);
    }

    /**
     * Associe une action à un bouton de la souris.
     * @param button Code du bouton.
     * @param action Action à associer.
     */
    public void bindMouseButton(int button, InputAction action) {
        mouseActions.put(button, action);
    }

    /**
     * Nettoie les ressources du gestionnaire des entrées.
     */
    public void cleanup() {
        keyStates.clear();
        mouseButtonStates.clear();
        inputListeners.clear();
        keyActions.clear();
        mouseActions.clear();
    }

    public InputManager getKeyboard() {
        return this;
    }

    // --- Interface pour les observateurs d'entrées ---
    public interface InputListener {
        void onInputUpdate(InputManager inputManager);
        void onKeyPress(String key);
        void onKeyRelease(String key);
        void onMousePress(int button);
        void onMouseRelease(int button);
        void onMouseMove(float mouseX, float mouseY);
        void onMouseScroll(float deltaX, float deltaY);
    }

    // --- Interface pour les actions d'entrées ---
    public interface InputAction {
        void execute();
    }
}
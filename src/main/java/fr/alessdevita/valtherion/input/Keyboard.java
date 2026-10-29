package fr.alessdevita.valtherion.input;

import java.util.*;

/**
 * Classe pour gérer le clavier dans Valtherion.
 * Fournit une abstraction pour les entrées du clavier.
 */
public class Keyboard {

    // Gestionnaire des entrées
    private final InputManager inputManager;

    // État des touches du clavier
    private final Map<String, Boolean> keyStates;

    // Liste des observateurs du clavier
    private final List<KeyboardListener> keyboardListeners;

    // Liste des combinaisons de touches
    private final Map<String, KeyCombination> keyCombinations;

    /**
     * Constructeur du gestionnaire du clavier.
     * @param inputManager Gestionnaire des entrées.
     */
    public Keyboard(InputManager inputManager) {
        this.inputManager = inputManager;
        this.keyStates = new HashMap<>();
        this.keyboardListeners = new ArrayList<>();
        this.keyCombinations = new HashMap<>();
    }

    /**
     * Initialise le gestionnaire du clavier.
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
        registerKey("Q");
        registerKey("R");
        registerKey("F");
        registerKey("1");
        registerKey("2");
        registerKey("3");

        System.out.println("Gestionnaire du clavier initialisé.");
    }

    /**
     * Enregistre une touche.
     * @param key Nom de la touche.
     */
    public void registerKey(String key) {
        keyStates.put(key, false);
    }

    /**
     * Met à jour le gestionnaire du clavier.
     */
    public void update() {
        // Mettre à jour les états des touches
        for (Map.Entry<String, Boolean> entry : keyStates.entrySet()) {
            String key = entry.getKey();
            boolean isPressed = inputManager.isKeyDown(key);

            // Si l'état a changé, notifier les observateurs
            if (isPressed != entry.getValue()) {
                entry.setValue(isPressed);

                for (KeyboardListener listener : keyboardListeners) {
                    if (isPressed) {
                        listener.onKeyPress(key);
                    } else {
                        listener.onKeyRelease(key);
                    }
                }
            }
        }

        // Vérifier les combinaisons de touches
        checkKeyCombinations();
    }

    /**
     * Vérifie les combinaisons de touches.
     */
    private void checkKeyCombinations() {
        for (KeyCombination combination : keyCombinations.values()) {
            boolean isPressed = true;

            // Vérifier si toutes les touches de la combinaison sont enfoncées
            for (String key : combination.getKeys()) {
                if (!isKeyDown(key)) {
                    isPressed = false;
                    break;
                }
            }

            // Si la combinaison est pressée, exécuter l'action
            if (isPressed && !combination.isPressed()) {
                combination.setPressed(true);
                combination.execute();
            } else if (!isPressed && combination.isPressed()) {
                combination.setPressed(false);
            }
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
     * Ajoute un observateur du clavier.
     * @param listener Observateur à ajouter.
     */
    public void addKeyboardListener(KeyboardListener listener) {
        keyboardListeners.add(listener);
    }

    /**
     * Retire un observateur du clavier.
     * @param listener Observateur à retirer.
     */
    public void removeKeyboardListener(KeyboardListener listener) {
        keyboardListeners.remove(listener);
    }

    /**
     * Enregistre une combinaison de touches.
     * @param name Nom de la combinaison.
     * @param keys Touches de la combinaison.
     * @param action Action à exécuter.
     */
    public void registerKeyCombination(String name, List<String> keys, Runnable action) {
        KeyCombination combination = new KeyCombination(name, keys, action);
        keyCombinations.put(name, combination);
    }

    /**
     * Récupère une combinaison de touches.
     * @param name Nom de la combinaison.
     * @return Combinaison de touches ou null si non trouvée.
     */
    public KeyCombination getKeyCombination(String name) {
        return keyCombinations.get(name);
    }

    /**
     * Nettoie les ressources du gestionnaire du clavier.
     */
    public void cleanup() {
        keyStates.clear();
        keyboardListeners.clear();
        keyCombinations.clear();
    }

    // --- Interface pour les observateurs du clavier ---
    public interface KeyboardListener {
        void onKeyPress(String key);
        void onKeyRelease(String key);
    }

    // --- Classe pour les combinaisons de touches ---
    public static class KeyCombination {
        private final String name;
        private final List<String> keys;
        private final Runnable action;
        private boolean isPressed;

        public KeyCombination(String name, List<String> keys, Runnable action) {
            this.name = name;
            this.keys = keys;
            this.action = action;
            this.isPressed = false;
        }

        public void execute() {
            if (action != null) {
                action.run();
            }
        }

        // --- Getters et Setters ---

        public String getName() {
            return name;
        }

        public List<String> getKeys() {
            return keys;
        }

        public boolean isPressed() {
            return isPressed;
        }

        public void setPressed(boolean pressed) {
            isPressed = pressed;
        }
    }
}
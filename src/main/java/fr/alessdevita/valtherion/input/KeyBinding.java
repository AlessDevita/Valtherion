package fr.alessdevita.valtherion.input;

import java.util.*;

/**
 * Classe pour gérer les bindings des touches dans Valtherion.
 * Permet d'associer des actions à des touches spécifiques.
 */
public class KeyBinding {

    // Nom du binding
    private final String name;

    // Touche associée au binding
    private String key;

    // Action associée au binding
    private InputManager.InputAction action;

    // Description du binding
    private String description;

    // Catégorie du binding (ex: MOVEMENT, COMBAT, INVENTORY, etc.)
    private String category;

    // Indique si le binding est activé
    private boolean isEnabled;

    // Liste des observateurs de binding
    private final List<KeyBindingListener> bindingListeners;

    /**
     * Constructeur du binding de touche.
     * @param name Nom du binding.
     * @param key Touche associée.
     * @param action Action associée.
     */
    public KeyBinding(String name, String key, InputManager.InputAction action) {
        this.name = name;
        this.key = key;
        this.action = action;
        this.description = "";
        this.category = "DEFAULT";
        this.isEnabled = true;
        this.bindingListeners = new ArrayList<>();
    }

    /**
     * Constructeur du binding de touche avec description et catégorie.
     * @param name Nom du binding.
     * @param key Touche associée.
     * @param action Action associée.
     * @param description Description du binding.
     * @param category Catégorie du binding.
     */
    public KeyBinding(String name, String key, InputManager.InputAction action,
                      String description, String category) {
        this.name = name;
        this.key = key;
        this.action = action;
        this.description = description;
        this.category = category;
        this.isEnabled = true;
        this.bindingListeners = new ArrayList<>();
    }

    /**
     * Exécute l'action associée au binding.
     */
    public void execute() {
        if (isEnabled && action != null) {
            action.execute();

            // Notifier les observateurs
            for (KeyBindingListener listener : bindingListeners) {
                listener.onKeyBindingExecute(this);
            }
        }
    }

    /**
     * Ajoute un observateur de binding.
     * @param listener Observateur à ajouter.
     */
    public void addBindingListener(KeyBindingListener listener) {
        bindingListeners.add(listener);
    }

    /**
     * Retire un observateur de binding.
     * @param listener Observateur à retirer.
     */
    public void removeBindingListener(KeyBindingListener listener) {
        bindingListeners.remove(listener);
    }

    // --- Getters et Setters ---

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public InputManager.InputAction getAction() {
        return action;
    }

    public void setAction(InputManager.InputAction action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    // --- Interface pour les observateurs de binding ---
    public interface KeyBindingListener {
        void onKeyBindingExecute(KeyBinding binding);
    }
}
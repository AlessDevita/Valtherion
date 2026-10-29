package fr.alessdevita.valtherion.input;

import java.util.*;

/**
 * Classe pour gérer les bindings des touches dans Valtherion.
 * Permet de charger, sauvegarder, et modifier les bindings.
 */
public class KeyBindingManager {

    // Map des bindings (nom -> binding)
    private final Map<String, KeyBinding> bindings;

    // Gestionnaire des entrées
    private final InputManager inputManager;

    // Fichier de configuration des bindings
    private String configFile;

    /**
     * Constructeur du gestionnaire des bindings.
     * @param inputManager Gestionnaire des entrées.
     */
    public KeyBindingManager(InputManager inputManager) {
        this.bindings = new HashMap<>();
        this.inputManager = inputManager;
        this.configFile = "config/keybindings.json";
    }

    /**
     * Initialise les bindings par défaut.
     */
    public void initDefaultBindings() {
        // Bindings de mouvement
        registerBinding(new KeyBinding(
                "MOVE_FORWARD", "W",
                () -> System.out.println("Avancer"),
                "Avancer", "MOVEMENT"
        ));

        registerBinding(new KeyBinding(
                "MOVE_BACKWARD", "S",
                () -> System.out.println("Reculer"),
                "Reculer", "MOVEMENT"
        ));

        registerBinding(new KeyBinding(
                "MOVE_LEFT", "A",
                () -> System.out.println("Aller à gauche"),
                "Aller à gauche", "MOVEMENT"
        ));

        registerBinding(new KeyBinding(
                "MOVE_RIGHT", "D",
                () -> System.out.println("Aller à droite"),
                "Aller à droite", "MOVEMENT"
        ));

        // Bindings d'actions
        registerBinding(new KeyBinding(
                "JUMP", "SPACE",
                () -> System.out.println("Sauter"),
                "Sauter", "ACTION"
        ));

        registerBinding(new KeyBinding(
                "SPRINT", "LEFT_SHIFT",
                () -> System.out.println("Sprinter"),
                "Sprinter", "ACTION"
        ));

        // Bindings d'inventaire
        registerBinding(new KeyBinding(
                "OPEN_INVENTORY", "E",
                () -> System.out.println("Ouvrir l'inventaire"),
                "Ouvrir l'inventaire", "INVENTORY"
        ));

        // Bindings de combat
        registerBinding(new KeyBinding(
                "ATTACK", "MOUSE_BUTTON_0",
                () -> System.out.println("Attaquer"),
                "Attaquer", "COMBAT"
        ));

        // Lier les bindings au gestionnaire des entrées
        bindAll();
    }

    /**
     * Enregistre un binding.
     * @param binding Binding à enregistrer.
     */
    public void registerBinding(KeyBinding binding) {
        bindings.put(binding.getName(), binding);
    }

    /**
     * Lie tous les bindings au gestionnaire des entrées.
     */
    public void bindAll() {
        for (KeyBinding binding : bindings.values()) {
            inputManager.bindKey(binding.getKey(), binding::execute);
        }
    }

    /**
     * Lie un binding spécifique au gestionnaire des entrées.
     * @param bindingName Nom du binding.
     */
    public void bind(String bindingName) {
        KeyBinding binding = bindings.get(bindingName);
        if (binding != null) {
            inputManager.bindKey(binding.getKey(), binding::execute);
        }
    }

    /**
     * Délie un binding du gestionnaire des entrées.
     * @param bindingName Nom du binding.
     */
    public void unbind(String bindingName) {
        KeyBinding binding = bindings.get(bindingName);
        if (binding != null) {
            inputManager.bindKey(binding.getKey(), null);
        }
    }

    /**
     * Récupère un binding par son nom.
     * @param name Nom du binding.
     * @return Binding ou null si non trouvé.
     */
    public KeyBinding getBinding(String name) {
        return bindings.get(name);
    }

    /**
     * Récupère tous les bindings.
     * @return Map des bindings.
     */
    public Map<String, KeyBinding> getBindings() {
        return bindings;
    }

    /**
     * Récupère les bindings d'une catégorie spécifique.
     * @param category Catégorie des bindings.
     * @return Liste des bindings de la catégorie.
     */
    public List<KeyBinding> getBindingsByCategory(String category) {
        List<KeyBinding> categoryBindings = new ArrayList<>();
        for (KeyBinding binding : bindings.values()) {
            if (binding.getCategory().equals(category)) {
                categoryBindings.add(binding);
            }
        }
        return categoryBindings;
    }

    /**
     * Modifie la touche associée à un binding.
     * @param bindingName Nom du binding.
     * @param newKey Nouvelle touche.
     */
    public void rebind(String bindingName, String newKey) {
        KeyBinding binding = bindings.get(bindingName);
        if (binding != null) {
            // Dé-lier l'ancienne touche
            inputManager.bindKey(binding.getKey(), null);

            // Mettre à jour la touche du binding
            binding.setKey(newKey);

            // Lier la nouvelle touche
            inputManager.bindKey(newKey, binding::execute);
        }
    }

    /**
     * Réinitialise les bindings aux valeurs par défaut.
     */
    public void resetToDefaults() {
        bindings.clear();
        initDefaultBindings();
    }

    /**
     * Charge les bindings depuis un fichier de configuration.
     */
    public void loadFromFile() {
        // Logique pour charger les bindings depuis un fichier JSON
        // Exemple : Utiliser org.json.simple pour lire le fichier
        System.out.println("Chargement des bindings depuis : " + configFile);
    }

    /**
     * Sauvegarde les bindings dans un fichier de configuration.
     */
    public void saveToFile() {
        // Logique pour sauvegarder les bindings dans un fichier JSON
        // Exemple : Utiliser org.json.simple pour écrire le fichier
        System.out.println("Sauvegarde des bindings dans : " + configFile);
    }

    /**
     * Définit le fichier de configuration des bindings.
     * @param configFile Chemin du fichier de configuration.
     */
    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }
}
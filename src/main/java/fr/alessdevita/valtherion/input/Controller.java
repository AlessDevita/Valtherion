package fr.alessdevita.valtherion.input;

import java.util.*;

/**
 * Classe pour gérer les manettes dans Valtherion.
 * Fournit une abstraction pour les entrées des manettes (boutons, sticks, déclencheurs).
 */
public class Controller {

    // Liste des manettes connectées
    private final List<GameController> controllers;

    // Manette active (par défaut, la première connectée)
    private GameController activeController;

    // Gestionnaire des entrées
    private final InputManager inputManager;

    /**
     * Constructeur du gestionnaire des manettes.
     * @param inputManager Gestionnaire des entrées.
     */
    public Controller(InputManager inputManager) {
        this.controllers = new ArrayList<>();
        this.inputManager = inputManager;
        this.activeController = null;
    }

    /**
     * Initialise le gestionnaire des manettes.
     */
    public void init() {
        // Détecter les manettes connectées
        detectControllers();

        // Définir la manette active
        if (!controllers.isEmpty()) {
            activeController = controllers.get(0);
        }

        System.out.println("Gestionnaire des manettes initialisé.");
    }

    /**
     * Détecte les manettes connectées.
     */
    private void detectControllers() {
        // Logique pour détecter les manettes connectées
        // Exemple : Utiliser LWJGL pour détecter les manettes
        // Pour l'instant, on simule la détection de 2 manettes
        GameController controller1 = new GameController("Manette 1");
        controller1.init();
        controllers.add(controller1);

        GameController controller2 = new GameController("Manette 2");
        controller2.init();
        controllers.add(controller2);

        System.out.println("Détection de " + controllers.size() + " manette(s).");
    }

    /**
     * Met à jour le gestionnaire des manettes.
     */
    public void update() {
        // Mettre à jour toutes les manettes
        for (GameController controller : controllers) {
            controller.update();
        }
    }

    /**
     * Ajoute une manette.
     * @param controller Manette à ajouter.
     */
    public void addController(GameController controller) {
        controllers.add(controller);

        // Si aucune manette n'est active, définir celle-ci comme active
        if (activeController == null) {
            activeController = controller;
        }
    }

    /**
     * Retire une manette.
     * @param controller Manette à retirer.
     */
    public void removeController(GameController controller) {
        controllers.remove(controller);

        // Si la manette retirée était active, définir une nouvelle manette active
        if (activeController == controller) {
            activeController = controllers.isEmpty() ? null : controllers.get(0);
        }
    }

    /**
     * Définit la manette active.
     * @param index Index de la manette.
     */
    public void setActiveController(int index) {
        if (index >= 0 && index < controllers.size()) {
            activeController = controllers.get(index);
        }
    }

    /**
     * Vérifie si une manette est connectée.
     * @param index Index de la manette.
     * @return true si la manette est connectée, false sinon.
     */
    public boolean isControllerConnected(int index) {
        return index >= 0 && index < controllers.size() && controllers.get(index).isConnected();
    }

    /**
     * Vérifie si un bouton est enfoncé sur la manette active.
     * @param button Code du bouton.
     * @return true si le bouton est enfoncé, false sinon.
     */
    public boolean isButtonDown(int button) {
        return activeController != null && activeController.isButtonDown(button);
    }

    /**
     * Récupère l'état d'un stick sur la manette active.
     * @param stick Code du stick.
     * @return Tableau avec les valeurs X et Y du stick.
     */
    public float[] getStickState(int stick) {
        return activeController != null ? activeController.getStickState(stick) : new float[]{0.0f, 0.0f};
    }

    /**
     * Récupère la valeur d'un déclencheur sur la manette active.
     * @param trigger Code du déclencheur.
     * @return Valeur du déclencheur (0.0 à 1.0).
     */
    public float getTriggerState(int trigger) {
        return activeController != null ? activeController.getTriggerState(trigger) : 0.0f;
    }

    /**
     * Définit la sensibilité des sticks pour toutes les manettes.
     * @param sensitivity Sensibilité des sticks.
     */
    public void setStickSensitivity(float sensitivity) {
        for (GameController controller : controllers) {
            controller.setStickSensitivity(sensitivity);
        }
    }

    /**
     * Définit le seuil des sticks pour toutes les manettes.
     * @param deadzone Seuil des sticks.
     */
    public void setStickDeadzone(float deadzone) {
        for (GameController controller : controllers) {
            controller.setStickDeadzone(deadzone);
        }
    }

    // --- Getters ---

    public List<GameController> getControllers() {
        return controllers;
    }

    public GameController getActiveController() {
        return activeController;
    }

    public int getControllerCount() {
        return controllers.size();
    }
}
package fr.alessdevita.valtherion.input;

import java.util.*;

/**
 * Classe pour gérer les entrées de manette dans Valtherion.
 * Gère les boutons, les sticks analogiques, et les déclencheurs.
 */
public class GameController {

    // Nom de la manette
    private String name;

    // État des boutons de la manette
    private final Map<Integer, Boolean> buttonStates;

    // État des sticks analogiques
    private final Map<Integer, float[]> stickStates;

    // État des déclencheurs (gâchettes)
    private final Map<Integer, Float> triggerStates;

    // Indique si la manette est connectée
    private boolean isConnected;

    // Sensibilité des sticks
    private float stickSensitivity;

    // Seuil pour les sticks (pour éviter les petites valeurs)
    private float stickDeadzone;

    /**
     * Constructeur de la manette.
     * @param name Nom de la manette.
     */
    public GameController(String name) {
        this.name = name;
        this.buttonStates = new HashMap<>();
        this.stickStates = new HashMap<>();
        this.triggerStates = new HashMap<>();
        this.isConnected = false;
        this.stickSensitivity = 1.0f;
        this.stickDeadzone = 0.1f;
    }

    /**
     * Initialise la manette.
     */
    public void init() {
        // Initialiser les états des boutons, sticks, et déclencheurs
        for (int i = 0; i < 20; i++) {
            buttonStates.put(i, false);
        }
        for (int i = 0; i < 4; i++) {
            stickStates.put(i, new float[]{0.0f, 0.0f});
        }
        for (int i = 0; i < 4; i++) {
            triggerStates.put(i, 0.0f);
        }

        isConnected = true;
        System.out.println("Manette initialisée : " + name);
    }

    /**
     * Met à jour la manette.
     */
    public void update() {
        // Pas de mise à jour nécessaire pour la manette
    }

    /**
     * Appelé lorsque un bouton de la manette est pressé.
     * @param button Code du bouton.
     */
    public void onButtonPress(int button) {
        buttonStates.put(button, true);
    }

    /**
     * Appelé lorsque un bouton de la manette est relâché.
     * @param button Code du bouton.
     */
    public void onButtonRelease(int button) {
        buttonStates.put(button, false);
    }

    /**
     * Appelé lorsque un stick analogique est déplacé.
     * @param stick Code du stick.
     * @param x Valeur X du stick.
     * @param y Valeur Y du stick.
     */
    public void onStickMove(int stick, float x, float y) {
        // Appliquer le seuil pour éviter les petites valeurs
        if (Math.abs(x) < stickDeadzone) {
            x = 0.0f;
        }
        if (Math.abs(y) < stickDeadzone) {
            y = 0.0f;
        }

        // Appliquer la sensibilité
        x *= stickSensitivity;
        y *= stickSensitivity;

        stickStates.put(stick, new float[]{x, y});
    }

    /**
     * Appelé lorsque un déclencheur est pressé.
     * @param trigger Code du déclencheur.
     * @param value Valeur du déclencheur (0.0 à 1.0).
     */
    public void onTriggerPress(int trigger, float value) {
        triggerStates.put(trigger, value);
    }

    /**
     * Vérifie si un bouton de la manette est enfoncé.
     * @param button Code du bouton.
     * @return true si le bouton est enfoncé, false sinon.
     */
    public boolean isButtonDown(int button) {
        return buttonStates.getOrDefault(button, false);
    }

    /**
     * Récupère l'état d'un stick analogique.
     * @param stick Code du stick.
     * @return Tableau avec les valeurs X et Y du stick.
     */
    public float[] getStickState(int stick) {
        return stickStates.getOrDefault(stick, new float[]{0.0f, 0.0f});
    }

    /**
     * Récupère la valeur d'un déclencheur.
     * @param trigger Code du déclencheur.
     * @return Valeur du déclencheur (0.0 à 1.0).
     */
    public float getTriggerState(int trigger) {
        return triggerStates.getOrDefault(trigger, 0.0f);
    }

    /**
     * Définit la sensibilité des sticks.
     * @param sensitivity Sensibilité des sticks.
     */
    public void setStickSensitivity(float sensitivity) {
        this.stickSensitivity = sensitivity;
    }

    /**
     * Définit le seuil des sticks.
     * @param deadzone Seuil des sticks.
     */
    public void setStickDeadzone(float deadzone) {
        this.stickDeadzone = deadzone;
    }

    // --- Getters ---

    public String getName() {
        return name;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public float getStickSensitivity() {
        return stickSensitivity;
    }

    public float getStickDeadzone() {
        return stickDeadzone;
    }
}